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
	public void emptyView() throws Exception {
		assertViewStateGenerated(
				forResource("view/EmptyView.java"),
				createDummyPresenter("view.EmptyView"),
				forResource("view/EmptyView$$State.java")
		);
	}

	@Test
	public void simpleView() throws Exception {
		assertViewStateGenerated(
				forResource("view/SimpleView.java"),
				createDummyPresenter("view.SimpleView"),
				forResource("view/SimpleView$$State.java")
		);
	}

	@Test
	public void overloading() throws Exception {
		assertViewStateGenerated(
				forResource("view/OverloadingView.java"),
				createDummyPresenter("view.OverloadingView"),
				forResource("view/OverloadingView$$State.java")
		);
	}

	@Test
	public void strategies() throws Exception {
		assertViewStateGenerated(
				forResource("view/StrategiesView.java"),
				createDummyPresenter("view.StrategiesView"),
				forResource("view/StrategiesView$$State.java")
		);
	}

	@Test
	public void generics() throws Exception {
		assertViewStateGenerated(
				forResource("view/GenericView.java"),
				createDummyPresenter("view.GenericView"),
				forResource("view/GenericView$$State.java")
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
	                                      JavaFileObject exceptedViewState) throws Exception {
		Compilation compilation = compileSourcesWithProcessor(view, presenter);

		assertThat(compilation).succeededWithoutWarnings();
		assertOutputFilesEquals(compilation.generatedFiles(), compileSources(view, exceptedViewState).generatedFiles());
	}
}
