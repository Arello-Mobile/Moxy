package com.arellomobile.mvp.compiler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.tools.Diagnostic;


import static org.junit.Assert.fail;

/**
 * Date: 20.02.2016
 * Time: 16:55
 *
 * @author Savin Mikhail
 */
public class ParamsHolderClassTest extends CompilerTest {
	@Test
	public void checkSeveralParams_throw() {
		try {
			getThat(JavaFileObjects.forResource("params/SeveralMethodParams.java"), JavaFileObjects.forResource("view/SeveralMethodParamsView.java"))
					.compilesWithoutError();
			Assert.fail();
		} catch (RuntimeException e) {
			Truth.assertThat(e.getLocalizedMessage().startsWith("Your params provider interface should contains only one methods, annotated as "));
		}
	}

	@Test
	public void checkEmptyParams_throw() {
		try {
			getThat(JavaFileObjects.forResource("view/EmptyParams.java"), JavaFileObjects.forResource("view/EmptyParamsView.java"))
					.compilesWithoutError();
			Assert.fail();
		} catch (RuntimeException e) {
			Truth.assertThat(e.getLocalizedMessage().startsWith("Your params provider interface should contains only one methods, annotated as "));
		}
	}

	@Test
	public void checkIncorrectParametersForMethod_throw() {
		try {
			getThat(JavaFileObjects.forResource("params/IncorrectParametersParams.java"), JavaFileObjects.forResource("view/IncorrectParametersParamsView.java")).compilesWithoutError();
			Assert.fail();
		} catch (RuntimeException e) {
			Truth.assertThat(e.getLocalizedMessage().startsWith("Your params provider interface should contains only one methods, annotated as "));
		}
	}

	@Test
	public void checkIncorrectCountOfParametersForMethod_throw() {
		try {
			getThat(JavaFileObjects.forResource("params/IncorrectCountOfParametersParams.java"), JavaFileObjects.forResource("view/IncorrectCountOfParametersParamsView.java")).compilesWithoutError();
			Assert.fail();
		} catch (RuntimeException e) {
			Truth.assertThat(e.getLocalizedMessage().startsWith("Your params provider interface should contains only one methods, annotated as "));
		}
	}

	//Uncomment this when [MOXY-22] will be fixed
	//TODO change message text to correct
	//	@Test
	public void checkWarningWhenImplementSeveralParamsProviders() {
		Pattern warningPattern = Pattern.compile("expected error");
		ImmutableTable<Diagnostic.Kind, Integer, Pattern> expectedDiagnostics = ImmutableTable.of(Diagnostic.Kind.WARNING, 16, warningPattern);
		try {
			assertCompilationResultIs(expectedDiagnostics, ImmutableList.of(getString("params/Params1.java"), getString("params/Params2.java"), getString("view/SeveralParamsView.java")));
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	//Uncomment this when [MOXY-25] will be fixed
	//	@Test
	public void checkPositiveParamsProviders() {
		ImmutableTable<Diagnostic.Kind, Integer, Pattern> expectedDiagnostics = ImmutableTable.of();
		try {
			assertCompilationResultIs(expectedDiagnostics, ImmutableList.of(getString("params/Params1.java"), getString("presenter/PositiveParamsViewPresenter.java"), getString("view/PositiveParamsView.java")));
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
}
