package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.MvpProcessor;
import com.google.testing.compile.Compilation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forSourceString;

/**
 * @author Evgeny Kursakov
 */
@RunWith(Parameterized.class)
public class ViewStateClassTest extends CompilerTest {

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
		return forSourceString("presenter.DummyPresenter", "" +
				"package presenter;\n" +
				"import com.arellomobile.mvp.InjectViewState;\n" +
				"import com.arellomobile.mvp.MvpPresenter;\n" +
				"@InjectViewState\n" +
				"public class DummyPresenter extends MvpPresenter<" + viewClass + "> {}");
	}
}
