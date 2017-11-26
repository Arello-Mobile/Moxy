package com.arellomobile.mvp.compiler;

import com.google.testing.compile.Compilation;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;
import static com.google.testing.compile.JavaFileObjects.forSourceString;

/**
 * @author Evgeny Kursakov
 */
public class ViewStateClassTest extends CompilerTest {

	@Test
	public void emptyView() {
		assertViewStateGenerated(
				forResource("view/EmptyView.java"),
				createDummyPresenter("view.EmptyView"),
				forResource("view/EmptyView$$State.java")
		);
	}

	@Test
	public void simpleView() {
		assertViewStateGenerated(
				forResource("view/SimpleView.java"),
				createDummyPresenter("view.SimpleView"),
				forResource("view/SimpleView$$State.java")
		);
	}

	@Test
	public void overloading() {
		assertViewStateGenerated(
				forResource("view/OverloadingView.java"),
				createDummyPresenter("view.OverloadingView"),
				forResource("view/OverloadingView$$State.java")
		);
	}

	@Test
	public void strategies() {
		assertViewStateGenerated(
				forResource("view/StrategiesView.java"),
				createDummyPresenter("view.StrategiesView"),
				forResource("view/StrategiesView$$State.java")
		);
	}

	private JavaFileObject createDummyPresenter(String viewClass) {
		return forSourceString("presenter.EmptyPresenter", "" +
				"package presenter;\n" +
				"import com.arellomobile.mvp.InjectViewState;\n" +
				"import com.arellomobile.mvp.MvpPresenter;\n" +
				"@InjectViewState\n" +
				"public class EmptyPresenter extends MvpPresenter<" + viewClass + "> {}");
	}

	private void assertViewStateGenerated(JavaFileObject view,
	                                      JavaFileObject presenter,
	                                      JavaFileObject exceptedViewState) {
		Compilation compilation = compileSourcesWithProcessor(view, presenter);

		assertThat(compilation).succeededWithoutWarnings();
		assertGeneratedFilesEquals(compilation.generatedFiles(), compileSources(view, exceptedViewState).generatedFiles());
	}
}
