package com.arellomobile.mvp.compiler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.Diagnostic;


import static org.junit.Assert.fail;

/**
 * Date: 26.02.2016
 * Time: 10:50
 *
 * @author Savin Mikhail
 */
public class ViewStateClassTest extends CompilerTest
{

	@Test
	public void viewStateForClassView_throw()
	{
		try
		{
			getThat(JavaFileObjects.forResource("view/CounterTestView.java"), JavaFileObjects.forResource("presenter/InjectViewStateForClassPresenter.java")).failsToCompile();
			fail();
		}
		catch (RuntimeException e)
		{
			Truth.assertThat(e.getLocalizedMessage().contains("must be INTERFACE, or not mark it as"));
		}
	}

	@Test
	public void positiveViewState()
	{
		try
		{
			assertCompilationResultIs(ImmutableTable.<Diagnostic.Kind, Integer, Pattern>of(), ImmutableList.of(getString("com/arellomobile/mvp/view/PositiveViewStateView$$State.java")));
		}
		catch (IOException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void viewStateForGenericView_throw()
	{
		try
		{
			getThat(JavaFileObjects.forResource("presenter/InjectViewStateForGenericViewPresenter.java"), JavaFileObjects.forResource("view/ViewStateForGenericView.java")).compilesWithoutError();
		}
		catch (RuntimeException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void viewStateChildWithIncorrectStrategyView_throw()
	{
		try
		{
			getThat(JavaFileObjects.forResource("view/ViewStateParentStrategyClassView.java"), JavaFileObjects.forResource("view/ViewStateParentView.java"), JavaFileObjects.forResource("view/ViewStateChildWithIncorrectStrategyClassView.java"), JavaFileObjects.forResource("presenter/ViewStateChildWithIncorrectStrategyClassPresenter.java")).failsToCompile();
			fail();
		}
		catch (RuntimeException e)
		{
			Matcher matcher = Pattern.compile("Both \\w* and \\w* has method \\w*\\(.*\\) with difference strategies\\.").matcher(e.getLocalizedMessage());
			Truth.assertThat(matcher.matches());
		}

		try
		{
			getThat(JavaFileObjects.forResource("view/ViewStateParentStrategyTagView.java"), JavaFileObjects.forResource("view/ViewStateParentView.java"), JavaFileObjects.forResource("view/ViewStateChildWithIncorrectStrategyTagView.java"), JavaFileObjects.forResource("presenter/ViewStateChildWithIncorrectStrategyClassPresenter.java")).failsToCompile();
			fail();
		}
		catch (RuntimeException e)
		{
			Matcher matcher = Pattern.compile("Both \\w* and \\w* has method \\w*\\(.*\\) with difference tags\\.").matcher(e.getLocalizedMessage());
			Truth.assertThat(matcher.matches());
		}
	}
}
