package com.arellomobile.mvp.compiler;

import java.io.IOException;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;

import org.junit.Test;

import javax.tools.Diagnostic;


import static org.junit.Assert.fail;

/**
 * Date: 26.02.2016
 * Time: 17:38
 *
 * @author Savin Mikhail
 */
public class ViewStateProviderClassTest extends CompilerTest {
	@Test
	public void positiveViewStateProvider() {
		try {
			assertCompilationResultIs(ImmutableTable.<Diagnostic.Kind, Integer, Pattern>of(), ImmutableList.of(getString("com/arellomobile/mvp/presenter/PositiveViewStateProviderPresenter$$ViewStateProvider.java")));
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}
}
