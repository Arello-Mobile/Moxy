package com.arellomobile.mvp.compiler;

import java.io.IOException;
import java.util.Collections;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;


import static org.junit.Assert.fail;

/**
 * Date: 25.02.2016
 * Time: 11:37
 *
 * @author Savin Mikhail
 */
public class PresenterBinderClassTest extends CompilerTest {
	//Uncomment this when [MOXY-24] will be fixed
	//@Test
	public void injectPresenterForObject_throw() {
		getThat(JavaFileObjects.forResource("view/ObjectInjectPresenterView.java"))
				.failsToCompile();
	}

	//Uncomment this when [MOXY-22] will be fixed
	//TODO change message text to correct
	//	@Test
	public void injectPresenterIntoNotViewClass() {
		Pattern warningPattern = Pattern.compile("expected error");
		ImmutableTable<Diagnostic.Kind, Integer, Pattern> expectedDiagnostics = ImmutableTable.of(Diagnostic.Kind.WARNING, 13, warningPattern);
		try {
			assertCompilationResultIs(expectedDiagnostics, ImmutableList.of(getString("view/InjectPresenterAnnotationInsideNotMvpViewClass.java")));
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void injectPresenterWithGeneric_throw() {
		getThat(JavaFileObjects.forResource("presenter/WithViewGenericPresenter.java"), JavaFileObjects.forResource("view/InjectPresenterWithGenericViewIncorrect.java")).failsToCompile().withErrorContaining("You can not use @InjectPresenter in classes that are not View, which is typified target Presenter").in(JavaFileObjects.forResource("view/InjectPresenterWithGenericViewIncorrect.java")).onLine(18);
	}

	@Test
	public void injectPresenterWithGeneric() {
		getThat(JavaFileObjects.forResource("presenter/WithViewGenericPresenter.java"), JavaFileObjects.forResource("view/InjectPresenterWithGenericView.java")).compilesWithoutError();
	}

	@Test
	public void injectPresenterWithIncorrectView_throw() {
		getThat(JavaFileObjects.forResource("view/InjectPresenterWithIncorrectViewView.java"))
				.failsToCompile().withErrorContaining("You can not use @InjectPresenter in classes that are not View, which is typified target Presenter").in(JavaFileObjects.forResource("view/InjectPresenterWithIncorrectViewView.java")).onLine(15);
	}

	//Uncomment this when [MOXY-28] will be fixed
	// @Test
	public void injectPresenterWithoutEmptyConstructor_throw() {
		getThat(JavaFileObjects.forResource("view/InjectPresenterWithoutEmptyConstructorView.java"))
				.failsToCompile();
	}

	@Test
	public void injectPresenterWithIncorrectParams_throw() {
		getThat(JavaFileObjects.forResource("view/InjectPresenterWithIncorrectParamsView.java"))
				.failsToCompile();
	}

	//Uncomment this when [MOXY-29] will be fixed
	//TODO change message text to correct
	//	@Test
	public void injectPresenterTypeBehavior_throw() {
		JavaFileObject view = JavaFileObjects.forResource("view/InjectPresenterTypeBehaviorView.java");
		getThat(Collections.singletonList(new ErrorProcessor()), JavaFileObjects.forResource("presenter/PositiveParamsViewPresenter.java"), view)
				.failsToCompile()
				.withErrorContaining("expected error").in(view).onLine(19).and()//tag = "", type = PresenterType.LOCAL
				.withErrorContaining("expected error").in(view).onLine(28).and()//tag=""
				.withErrorContaining("expected error").in(view).onLine(22).and()//factory = MockPresenterFactory.class, type = PresenterType.LOCAL
				.withErrorContaining("expected error").in(view).onLine(31).and()//factory
				.withErrorContaining("expected error").in(view).onLine(25).and()//presenterId = "", type = PresenterType.LOCAL
				.withErrorContaining("expected error").in(view).onLine(34).and()//presenterId
				.withErrorContaining("expected error").in(view).onLine(37);//tag = "", factory = MockPresenterFactory.class
	}
}
