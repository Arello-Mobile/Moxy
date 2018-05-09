package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.MvpProcessor;
import com.google.testing.compile.Compilation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;

/**
 * @author Evgeny Kursakov
 */
@RunWith(Parameterized.class)
public class ViewStateTest extends CompilerTest {

	@Parameterized.Parameter
	public String viewClassName;

	@Parameterized.Parameters(name = "{0}")
	public static String[] data() {
		return new String[]{
				"view.EmptyView",
				"view.SimpleView",
				"view.OverloadingView",
				"view.StrategiesView",
				"view.GenericView",
				"view.GenericWithExtendsView",
				"view.GenericMethodsView",
				"view.strategies_inheritance.ChildView",
				"view.strategies_inheritance.ParentView",
		};
	}

	@Test
	public void test() throws Exception {
		JavaFileObject presenter = createDummyPresenter(viewClassName);
		JavaFileObject exceptedViewState = getSourceFile(viewClassName + MvpProcessor.VIEW_STATE_SUFFIX);

		Compilation presenterCompilation = compileSourcesWithProcessor(presenter);
		Compilation exceptedViewStateCompilation = compileSources(exceptedViewState);

		assertThat(presenterCompilation).succeededWithoutWarnings();
		assertExceptedFilesGenerated(presenterCompilation.generatedFiles(), exceptedViewStateCompilation.generatedFiles());
	}

	private JavaFileObject createDummyPresenter(String viewClass) {
		return forSourceLines("presenter.DummyPresenter",
				"package presenter;",
				"import com.arellomobile.mvp.InjectViewState;",
				"import com.arellomobile.mvp.MvpPresenter;",
				"@InjectViewState",
				"public class DummyPresenter extends MvpPresenter<" + viewClass + "> {}"
		);
	}
}
