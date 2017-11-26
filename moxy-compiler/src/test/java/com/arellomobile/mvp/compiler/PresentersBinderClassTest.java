package com.arellomobile.mvp.compiler;

import com.google.testing.compile.Compilation;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;

/**
 * @author Evgeny Kursakov
 */
public class PresentersBinderClassTest extends CompilerTest {

	@Test
	public void simpleInjectPresenterTarget() throws Exception {
		assertPresentersBinderGenerated(
				forResource("view/EmptyView.java"),
				forResource("presenter/EmptyViewPresenter.java"),
				forResource("target/SimpleInjectPresenterTarget.java"),
				forResource("target/SimpleInjectPresenterTarget$$PresentersBinder.java")
		);
	}

	@Test
	public void simpleProvidePresenterTarget() throws Exception {
		assertPresentersBinderGenerated(
				forResource("view/EmptyView.java"),
				forResource("presenter/EmptyViewPresenter.java"),
				forResource("target/SimpleProvidePresenterTarget.java"),
				forResource("target/SimpleProvidePresenterTarget$$PresentersBinder.java")
		);
	}

	private void assertPresentersBinderGenerated(JavaFileObject view,
	                                             JavaFileObject presenter,
	                                             JavaFileObject target,
	                                             JavaFileObject exceptedPresentersBinder) throws Exception {
		Compilation compilation = compileSourcesWithProcessor(presenter, view, target);

		assertThat(compilation).succeededWithoutWarnings();
		assertOutputFilesEquals(
				compilation.generatedFiles(),
				compileSources(view, presenter, target, exceptedPresentersBinder).generatedFiles()
		);
	}
}
