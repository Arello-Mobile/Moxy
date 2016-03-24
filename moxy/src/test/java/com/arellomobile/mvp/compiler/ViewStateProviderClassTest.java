package com.arellomobile.mvp.compiler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.tools.Diagnostic;

import static org.junit.Assert.fail;

/**
 * Date: 26.02.2016
 * Time: 17:38
 *
 * @author Savin Mikhail
 */
public class ViewStateProviderClassTest extends CompilerTest
{
	@Test
	public void positiveViewStateProvider()
	{
		try
		{
			assertCompilationResultIs(ImmutableTable.<Diagnostic.Kind, Integer, Pattern>of(), ImmutableList.of(getString("com/arellomobile/mvp/presenter/PositiveViewStateProviderPresenter$$ViewStateClassNameProvider.java")));
		}
		catch (IOException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void viewStateProviderForNotPresenter_throw()
	{
		try
		{
			getThat(JavaFileObjects.forResource("presenter/PositiveViewStateProviderForNotPresenter.java")).failsToCompile();
			fail();
		}
		catch (RuntimeException e)
		{
			Truth.assertThat(e.getLocalizedMessage().contains(" doesn't know, what is target MvpView type of this presenter."));
		}
	}

}
