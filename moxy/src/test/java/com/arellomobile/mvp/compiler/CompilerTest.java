package com.arellomobile.mvp.compiler;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaSourcesSubjectFactory;

import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Date: 25.02.2016
 * Time: 11:40
 *
 * @author Savin Mikhail
 */
public abstract class CompilerTest {
	private JavaCompiler javac;
	private DiagnosticCollector<JavaFileObject> diagnosticCollector;
	private StandardJavaFileManager fileManager;
	private File tmpDir;

	@Before
	public void setUp() {
		javac = ToolProvider.getSystemJavaCompiler();
		diagnosticCollector = new DiagnosticCollector<>();
		fileManager = javac.getStandardFileManager(diagnosticCollector, null, null);
		tmpDir = Files.createTempDir();
	}

	protected CompileTester getThat(final JavaFileObject... target) {
		return getThat(Collections.singletonList(new MvpCompiler()), target);
	}

	protected CompileTester getThat(Collection<? extends Processor> processors, final JavaFileObject... target) {
		return Truth.assert_().about(JavaSourcesSubjectFactory.javaSources())
				.that(Arrays.asList(target))
				.processedWith(processors);
	}

	protected String getString(String filePath) throws IOException {

		URL resource = Resources.getResource(filePath);
		Scanner scanner = new Scanner(new ByteArrayInputStream(Resources.toByteArray(resource)));
		StringBuilder stringBuilder = new StringBuilder();
		while (scanner.hasNextLine()) {
			stringBuilder.append(scanner.nextLine());
			stringBuilder.append("\n");
		}

		return stringBuilder.toString();
	}

	protected void assertCompilationResultIs(Table<Diagnostic.Kind, Integer, Pattern> expectedDiagnostics, Collection<String> resources) throws IOException {
		assertCompilationResultIs(expectedDiagnostics, resources, MvpCompiler.class);
	}

	//For more info see https://github.com/google/auto/blob/master/value/src/test/java/com/google/auto/value/processor/CompilationErrorsTest.java
	protected void assertCompilationResultIs(Table<Diagnostic.Kind, Integer, Pattern> expectedDiagnostics, Collection<String> resources, final Class<? extends Processor> processor) throws IOException {
		StringWriter compilerOut = new StringWriter();

		List<String> options = ImmutableList.of(
				"-sourcepath", tmpDir.getPath(),
				"-d", tmpDir.getPath(),
				"-processor", processor.getName(),
				"-Xlint");
		javac.getTask(compilerOut, fileManager, diagnosticCollector, options, null, null);
		// This doesn't compile anything but communicates the paths to the JavaFileManager.

		// Convert the strings containing the source code of the test classes into files that we
		// can feed to the compiler.
		List<String> classNames = Lists.newArrayList();
		List<JavaFileObject> sourceFiles = Lists.newArrayList();
		for (String source : resources) {
			ClassName className = ClassName.extractFromSource(source);
			File dir = new File(tmpDir, className.sourceDirectoryName());
			dir.mkdirs();
			assertTrue(dir.isDirectory());  // True if we just made it, or it was already there.
			String sourceName = className.simpleName + ".java";
			Files.write(source, new File(dir, sourceName), Charset.forName("UTF-8"));
			classNames.add(className.fullName());
			JavaFileObject sourceFile = fileManager.getJavaFileForInput(
					StandardLocation.SOURCE_PATH, className.fullName(), JavaFileObject.Kind.SOURCE);
			sourceFiles.add(sourceFile);
		}
		assertEquals(classNames.size(), sourceFiles.size());

		// Compile the classes.
		JavaCompiler.CompilationTask javacTask = javac.getTask(
				compilerOut, fileManager, diagnosticCollector, options, classNames, sourceFiles);
		boolean compiledOk = javacTask.call();

		// Check that there were no compilation errors unless we were expecting there to be.
		// We ignore "notes", typically debugging output from the annotation processor
		// when that is enabled.
		Table<Diagnostic.Kind, Integer, String> diagnostics = HashBasedTable.create();
		for (Diagnostic<?> diagnostic : diagnosticCollector.getDiagnostics()) {
			boolean ignore = (diagnostic.getKind() == Diagnostic.Kind.NOTE
			                  || (diagnostic.getKind() == Diagnostic.Kind.WARNING
			                      && diagnostic.getMessage(null).contains(
					"No processor claimed any of these annotations")));
			if (!ignore) {
				diagnostics.put(
						diagnostic.getKind(), (int) diagnostic.getLineNumber(), diagnostic.getMessage(null));
			}
		}
		assertEquals(diagnostics.containsRow(Diagnostic.Kind.ERROR), !compiledOk);
		assertEquals("Diagnostic kinds should match: " + diagnostics,
				expectedDiagnostics.rowKeySet(), diagnostics.rowKeySet());
		for (Table.Cell<Diagnostic.Kind, Integer, Pattern> expectedDiagnostic :
				expectedDiagnostics.cellSet()) {
			boolean match = false;
			for (Table.Cell<Diagnostic.Kind, Integer, String> diagnostic : diagnostics.cellSet()) {

				if (expectedDiagnostic.getValue().matcher(diagnostic.getValue()).find()) {
					int expectedLine = expectedDiagnostic.getColumnKey();
					if (expectedLine != 0) {
						int actualLine = diagnostic.getColumnKey();
						if (actualLine != expectedLine) {
							fail("Diagnostic matched pattern but on line " + actualLine
							     + " not line " + expectedLine + ": " + diagnostic.getValue());
						}
					}
					match = true;
					break;
				}
			}
			assertTrue("Diagnostics should contain " + expectedDiagnostic + ": " + diagnostics, match);
		}
	}

	private static class ClassName {
		final String packageName; // Package name with trailing dot. May be empty but not null.
		final String simpleName;

		private ClassName(String packageName, String simpleName) {
			this.packageName = packageName;
			this.simpleName = simpleName;
		}

		// Extract the package and simple name of the top-level class defined in the given string,
		// which is a Java sourceUnit unit.
		static ClassName extractFromSource(String sourceUnit) {
			String pkg;
			if (sourceUnit.contains("package ")) {
				// (?s) means that . matches everything including \n
				pkg = sourceUnit.replaceAll("(?s).*?package ([a-z.]+);.*", "$1") + ".";
			} else {
				pkg = "";
			}
			String cls = sourceUnit.replaceAll("(?s).*?(class|interface|enum) ([A-Za-z0-9_$]+).*", "$2");
			assertTrue(cls, cls.matches("[A-Za-z0-9_$]+"));
			return new ClassName(pkg, cls);
		}

		String fullName() {
			return packageName + simpleName;
		}

		String sourceDirectoryName() {
			return packageName.replace('.', '/');
		}
	}
}
