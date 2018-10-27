package com.arellomobile.mvp.compiler;

import com.google.testing.compile.Compilation;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;

public class MultiModulesTest extends CompilerTest {

	@Test
	public void testLibraryModule() throws Exception {
		JavaFileObject[] sources = {
				forResource("multimodules/lib1/Lib1Presenter.java"),
				forResource("multimodules/lib1/Lib1View.java")
		};

		JavaFileObject[] generatedSources = {
				forResource("multimodules/lib1/Lib1Presenter$$ViewStateProvider.java"),
				forResource("multimodules/lib1/Lib1View$$State.java"),
				forResource("multimodules/lib1/MoxyReflector.java")
		};

		Compilation compilation = compileLibSourcesWithProcessor("multimodules.lib1", sources);
		Compilation exceptedCompilation = compileSources(generatedSources);

		assertThat(compilation).succeededWithoutWarnings();
		assertExceptedFilesGenerated(compilation.generatedFiles(), exceptedCompilation.generatedFiles());
	}

	@Test
	public void testRegisterMoxyReflectorPackages() throws Exception {
		JavaFileObject someClientClass = forSourceLines("multimodules.app.App",
				"package multimodules.app;",
				"import com.arellomobile.mvp.RegisterMoxyReflectorPackages;",
				"@RegisterMoxyReflectorPackages(\"multimodules.lib1\")",
				"public class App {}"
		);

		JavaFileObject[] sources = new JavaFileObject[]{
				forResource("multimodules/app/AppPresenter.java"),
				forResource("multimodules/app/AppView.java"),
				someClientClass
		};

		JavaFileObject[] exceptedSources = new JavaFileObject[]{
				forResource("multimodules/app/AppPresenter$$ViewStateProvider.java"),
				forResource("multimodules/app/AppView$$State.java"),
				forResource("multimodules/app/MoxyReflector.java")
		};

		Compilation compilation = compileSourcesWithProcessor(sources);
		Compilation exceptedCompilation = compileSources(exceptedSources);

		assertThat(compilation).succeededWithoutWarnings();
		assertExceptedFilesGenerated(compilation.generatedFiles(), exceptedCompilation.generatedFiles());
	}
}
