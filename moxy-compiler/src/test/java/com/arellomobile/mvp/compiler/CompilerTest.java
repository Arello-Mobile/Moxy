package com.arellomobile.mvp.compiler;

import com.google.common.base.Joiner;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;

import junit.framework.AssertionFailedError;
import junit.framework.ComparisonFailure;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import static com.google.testing.compile.Compiler.javac;

/**
 * @author Evgeny Kursakov
 */
public abstract class CompilerTest {

	protected Compilation compileSources(JavaFileObject... sources) {
		return javac()
				.withOptions("-implicit:none") // don't process or generate classes for implicitly found sources
				.compile(sources);
	}

	protected Compilation compileSourcesWithProcessor(JavaFileObject... sources) {
		return javac()
				.withOptions("-implicit:none") // TODO: enable lint (-Xlint:processing)
				.withProcessors(new MvpCompiler())
				.compile(sources);
	}

	/**
	 * Asserts that all files from {@code exceptedGeneratedFiles} exists in {@code actualGeneratedFiles}
	 * and have equivalent bytecode
	 */
	protected void assertExceptedFilesGenerated(List<JavaFileObject> actualGeneratedFiles, List<JavaFileObject> exceptedGeneratedFiles) throws Exception {
		for (JavaFileObject exceptedClass : exceptedGeneratedFiles) {
			final String fileName = exceptedClass.getName();

			JavaFileObject actualClass = actualGeneratedFiles.stream()
					.filter(input -> fileName.equals(input.getName()))
					.findFirst()
					.orElseThrow(() -> new AssertionFailedError("File " + fileName + " is not generated"));

			String actualBytecode = getBytecodeString(actualClass);
			String exceptedBytecode = getBytecodeString(exceptedClass);

			if (!exceptedBytecode.equals(actualBytecode)) {
				JavaFileObject actualSource = findSourceForClass(actualGeneratedFiles, fileName);

				throw new ComparisonFailure(Joiner.on('\n').join(
						"Bytecode for file " + fileName + " not equal to excepted",
						"",
						"Actual generated file (" + actualSource.getName() + "):",
						"================",
						"",
						actualSource.getCharContent(false),
						""
				), exceptedBytecode, actualBytecode);
			}
		}
	}

	/**
	 * Find .java file in resources by full qualified class name
	 */
	protected JavaFileObject getSourceFile(String className) {
		return JavaFileObjects.forResource(className.replace('.', '/') + ".java");
	}

	private String getBytecodeString(JavaFileObject file) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ClassReader classReader = new ClassReader(file.openInputStream());
		TraceClassVisitor classVisitor = new TraceClassVisitor(new PrintWriter(out));
		classReader.accept(classVisitor, ClassReader.SKIP_DEBUG); // skip debug info (line numbers)
		return out.toString();
	}

	private JavaFileObject findSourceForClass(List<JavaFileObject> outputFiles, String classFileName) {
		// TODO: more effective algorithm ;)
		String sourceFile = classFileName
				.replace(StandardLocation.CLASS_OUTPUT.getName(), StandardLocation.SOURCE_OUTPUT.getName())
				.replace(".class", "");

		// remove chars from end of name to find parent class source
		int nameStart = sourceFile.lastIndexOf("/") + 1;
		for (int i = sourceFile.length(); i > nameStart; i--) {
			String name = sourceFile.substring(0, i) + ".java";

			Optional<JavaFileObject> file = outputFiles.stream()
					.filter(javaFileObject -> javaFileObject.getName().equals(name))
					.findFirst();

			if (file.isPresent()) return file.get();
		}

		throw new RuntimeException("Can't find generated source for class " + classFileName);
	}
}
