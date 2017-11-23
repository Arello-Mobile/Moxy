package com.arellomobile.mvp.compiler;

import com.google.testing.compile.Compilation;

import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;

/**
 * @author Evgeny Kursakov
 */
public class ViewStateClassTest extends CompilerTest {

	@Test
	public void simpleView() throws Exception {
		Compilation compilation = compileSourcesWithProcessor(
				forResource("view/SimpleView.java"),
				forResource("presenter/SimpleViewPresenter.java")
		);

		assertThat(compilation).succeededWithoutWarnings();
		assertFilesGenerated(compilation,
				forResource("view/SimpleView$$State.java")
		);
	}

	@Test
	public void overloading() throws Exception {
		Compilation compilation = compileSourcesWithProcessor(
				forResource("view/OverloadingView.java"),
				forResource("presenter/OverloadingViewPresenter.java")
		);

		assertThat(compilation).succeededWithoutWarnings();
		assertFilesGenerated(compilation,
				forResource("view/OverloadingView$$State.java")
		);
	}

	@Test
	public void strategies() throws Exception {
		Compilation compilation = compileSourcesWithProcessor(
				forResource("view/StrategiesView.java"),
				forResource("presenter/StrategiesViewPresenter.java")
		);

		assertThat(compilation).succeededWithoutWarnings();
		assertFilesGenerated(compilation,
				forResource("view/StrategiesView$$State.java")
		);
	}
}
