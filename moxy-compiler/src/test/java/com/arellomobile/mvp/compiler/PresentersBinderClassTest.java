package com.arellomobile.mvp.compiler;

import com.google.testing.compile.Compilation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static com.google.testing.compile.CompilationSubject.assertThat;

/**
 * @author Evgeny Kursakov
 */
@RunWith(Parameterized.class)
public class PresentersBinderClassTest extends CompilerTest {

	private final String targetResourceName;
	private final String exceptedPresentersBinderResourceName;

	public PresentersBinderClassTest(String targetResourceName, String exceptedPresentersBinderResourceName) {
		this.targetResourceName = targetResourceName;
		this.exceptedPresentersBinderResourceName = exceptedPresentersBinderResourceName;
	}

	@Parameterized.Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"target.SimpleInjectPresenterTarget", "target/SimpleInjectPresenterTarget$$PresentersBinder.java"},
				{"target.SimpleProvidePresenterTarget", "target/SimpleProvidePresenterTarget$$PresentersBinder.java"},
		});
	}

	@Test
	public void test() throws Exception {
		Compilation targetCompilation = compileSourcesWithProcessor(sourceByClassName(targetResourceName));
		Compilation exceptedPresentersBinderCompilation = compileSources(sourceByClassName(exceptedPresentersBinderResourceName));

		assertThat(targetCompilation).succeededWithoutWarnings();
		assertExceptedFilesGenerated(targetCompilation.generatedFiles(), exceptedPresentersBinderCompilation.generatedFiles());
	}
}
