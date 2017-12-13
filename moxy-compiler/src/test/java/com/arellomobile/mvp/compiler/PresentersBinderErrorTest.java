package com.arellomobile.mvp.compiler;

import com.google.testing.compile.Compilation;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;

/**
 * @author Evgeny Kursakov
 */
public class PresentersBinderErrorTest extends CompilerTest {

	@Test
	public void testNotImplementViewInterface() throws Exception {
		JavaFileObject target = forResource("target/NotImplementViewInterfaceTarget.java");

		Compilation targetCompilation = compileSourcesWithProcessor(target);

		assertThat(targetCompilation)
				.hadErrorContaining("You can not use @InjectPresenter in classes that are not View, which is typified target Presenter")
				.inFile(target)
				.onLineContaining("EmptyViewPresenter presenter");
	}
}
