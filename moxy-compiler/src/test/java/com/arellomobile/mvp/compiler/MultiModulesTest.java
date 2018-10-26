package com.arellomobile.mvp.compiler;

import com.google.testing.compile.Compilation;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;

public class MultiModulesTest extends CompilerTest {

	@Test
	public void testCompileLibraryModule() throws Exception {
		JavaFileObject presenter = forResource("multimodules/lib1/Lib1Presenter.java");
		JavaFileObject view = forResource("multimodules/lib1/Lib1View.java");

		Compilation compilation = compileLibSourcesWithProcessor("multimodules.lib1", presenter, view);
		Compilation exceptedCompilation = compileSources(
				forResource("multimodules/lib1/Lib1Presenter$$ViewStateProvider.java"),
				forResource("multimodules/lib1/Lib1View$$State.java"),
				forResource("multimodules/lib1/MoxyReflector.java")
		);

		assertThat(compilation).succeededWithoutWarnings();
		assertExceptedFilesGenerated(compilation.generatedFiles(), exceptedCompilation.generatedFiles());
	}
}
