package com.arellomobile.mvp.compiler;

import com.google.testing.compile.Compilation;

import junit.framework.AssertionFailedError;

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

	protected Compilation compileSources(JavaFileObject... sources) {
		return javac().compile(sources);
	}

	protected void assertGeneratedFilesEquals(List<JavaFileObject> actualGeneratedFiles, List<JavaFileObject> exceptedGeneratedFiles) {
		for (JavaFileObject exceptedFile : exceptedGeneratedFiles) {
			final String fileName = exceptedFile.getName();

			JavaFileObject actualFile = actualGeneratedFiles.stream()
					.filter(input -> fileName.equals(input.getName()))
					.findFirst()
					.orElseThrow(() -> new AssertionFailedError("File " + fileName + " is not generated"));

			String actualFileText = getBytecodeString(actualFile);
			String exceptedFileText = getBytecodeString(exceptedFile);

			assertThat(actualFileText)
					.named("Bytecode for file %s not equal to excepted", fileName)
					.isEqualTo(exceptedFileText);
		}
	}

	private String getBytecodeString(JavaFileObject file) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ClassReader classReader = new ClassReader(file.openInputStream());
			TraceClassVisitor classVisitor = new TraceClassVisitor(new PrintWriter(out));
			classReader.accept(classVisitor, ClassReader.SKIP_DEBUG); // skip debug info (line numbers)
			return out.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
