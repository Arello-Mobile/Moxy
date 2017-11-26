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
		assertViewStateGenerated("view.EmptyView", forResource("view/EmptyView$$State.java"));
	}

	@Test
	public void simpleView() throws Exception {
		assertViewStateGenerated("view.SimpleView", forResource("view/SimpleView$$State.java"));
	}

	@Test
	public void overloading() throws Exception {
		assertViewStateGenerated("view.OverloadingView", forResource("view/OverloadingView$$State.java"));
	}

	@Test
	public void strategies() throws Exception {
		assertViewStateGenerated("view.StrategiesView", forResource("view/StrategiesView$$State.java"));
	}

	@Test
	public void generics() throws Exception {
		assertViewStateGenerated("view.GenericView", forResource("view/GenericView$$State.java"));
	}

	private void assertViewStateGenerated(String viewClass, JavaFileObject exceptedViewState) throws Exception {
		Compilation presenterCompilation = compileSourcesWithProcessor(createDummyPresenter(viewClass));
		Compilation exceptedViewStateCompilation = compileSources(exceptedViewState);

		assertThat(presenterCompilation).succeededWithoutWarnings();
		assertExceptedFilesGenerated(presenterCompilation.generatedFiles(), exceptedViewStateCompilation.generatedFiles());
	}

	private JavaFileObject createDummyPresenter(String viewClass) {
		return forSourceString("presenter.EmptyPresenter", "" +
				"package presenter;\n" +
				"import com.arellomobile.mvp.InjectViewState;\n" +
				"import com.arellomobile.mvp.MvpPresenter;\n" +
				"@InjectViewState\n" +
				"public class EmptyPresenter extends MvpPresenter<" + viewClass + "> {}");
	}
}
