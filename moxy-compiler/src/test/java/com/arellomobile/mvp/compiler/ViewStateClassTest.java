package com.arellomobile.mvp.compiler;

import com.google.testing.compile.Compilation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;
import static com.google.testing.compile.JavaFileObjects.forSourceString;

/**
 * @author Evgeny Kursakov
 */
@RunWith(Parameterized.class)
public class ViewStateClassTest extends CompilerTest {

	private final String viewClassName;
	private final String exceptedViewStateResourceName;

	public ViewStateClassTest(String viewClassName, String exceptedViewStateResourceName) {
		this.viewClassName = viewClassName;
		this.exceptedViewStateResourceName = exceptedViewStateResourceName;
	}

	@Parameterized.Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"view.EmptyView", "view/EmptyView$$State.java"},
				{"view.SimpleView", "view/SimpleView$$State.java"},
				{"view.OverloadingView", "view/OverloadingView$$State.java"},
				{"view.StrategiesView", "view/StrategiesView$$State.java"},
				{"view.GenericView", "view/GenericView$$State.java"},
		});
	}

	@Test
	public void test() throws Exception {
		Compilation presenterCompilation = compileSourcesWithProcessor(createDummyPresenter(viewClassName));
		Compilation exceptedViewStateCompilation = compileSources(forResource(exceptedViewStateResourceName));

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
