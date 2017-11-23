package com.arellomobile.mvp.compiler;

import com.google.testing.compile.Compilation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.Compiler.javac;

/**
 * @author Evgeny Kursakov
 */
public abstract class CompilerTest {
	protected Compilation compileSourcesWithProcessor(JavaFileObject... sources) {
		return javac()
				.withProcessors(new MvpCompiler())
				.withOptions("-Xlint:-processing")
				.compile(sources);
	}

	protected void assertFilesGenerated(
			Compilation compilation,
			JavaFileObject... exceptedSources
	) throws Exception {
		List<JavaFileObject> generatedFiles = compilation.generatedFiles();
		List<JavaFileObject> exceptedFiles = javac().compile(exceptedSources).generatedFiles();

		for (final JavaFileObject exceptedFile : exceptedFiles) {
			JavaFileObject actualFile = generatedFiles.stream()
					.filter(input -> exceptedFile.toUri().equals(input.toUri()))
					.findFirst()
					.get();

			String actualFileText = getBytecodeString(actualFile);
			String exceptedFileText = getBytecodeString(exceptedFile);

			assertThat(actualFileText).isEqualTo(exceptedFileText);
		}
	}

	private String getBytecodeString(JavaFileObject file) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ClassReader classReader = new ClassReader(file.openInputStream());
		classReader.accept(new TraceClassVisitor(new PrintWriter(out)), ClassReader.SKIP_DEBUG);
		return out.toString();
	}
}
