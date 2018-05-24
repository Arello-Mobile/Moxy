package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.MvpProcessor;
import com.google.testing.compile.Compilation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

/**
 * @author Evgeny Kursakov
 */
@RunWith(Parameterized.class)
public class ViewStateProviderTest extends CompilerTest {

	@Parameterized.Parameter
	public String presenterClassName;

	@Parameterized.Parameters(name = "{0}")
	public static String[] data() {
		return new String[]{
				"presenter.EmptyViewPresenter",
				"presenter.GenericPresenter", // warning (Your GenericPresenter is typed...)
		};
	}

	@Test
	public void test() throws Exception {
		JavaFileObject presenter = getSourceFile(presenterClassName);
		JavaFileObject exceptedViewStateProvider = getSourceFile(presenterClassName + MvpProcessor.VIEW_STATE_PROVIDER_SUFFIX);

		Compilation presenterCompilation = compileSourcesWithProcessor(presenter);
		Compilation exceptedViewStateProviderCompilation = compileSources(exceptedViewStateProvider);

		assertThat(presenterCompilation).succeeded(); // TODO: assert no warnings
		assertExceptedFilesGenerated(presenterCompilation.generatedFiles(), exceptedViewStateProviderCompilation.generatedFiles());
	}
}
