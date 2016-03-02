package com.arellomobile.mvp.tests;

import android.os.Bundle;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.view.ChildView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;
import com.google.common.collect.ImmutableMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Date: 29.02.2016
 * Time: 9:12
 *
 * @author Savin Mikhail
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class StateStrategyTest
{
	@Test
	public void defaultStateStrategyTest()
	{
		ChildView childView = mock(ChildView.class);
		MvpDelegate<ChildView> mvpDelegate = new MvpDelegate<>(childView);
		mvpDelegate.onCreate(mock(Bundle.class));
		mvpDelegate.onStart();
		try
		{
			Object[] enumConstants = Class.forName("com.arellomobile.mvp.view.ChildView$$State$LocalViewCommand").getEnumConstants();
			Map<String, Class<? extends StateStrategy>> result = ImmutableMap.of("withoutStrategyMethod", SkipStrategy.class,
					"customStrategyMethod", AddToEndSingleStrategy.class,
					"parentOverrideMethodWithCustomStrategy", SkipStrategy.class,
					"simpleInterfaceMethod", SkipStrategy.class);
			checkStrategy(enumConstants, result);

			enumConstants = Class.forName("com.arellomobile.mvp.view.ParentView$$State$LocalViewCommand").getEnumConstants();
			result = ImmutableMap.of("withoutStrategyMethod", AddToEndStrategy.class,
					"parentOverrideMethodWithCustomStrategy", AddToEndSingleStrategy.class,
					"customStrategyMethod", AddToEndSingleStrategy.class);
			checkStrategy(enumConstants, result);
		}
		catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e)
		{
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private void checkStrategy(final Object[] enumConstants, final Map<String, Class<? extends StateStrategy>> result) throws NoSuchFieldException, IllegalAccessException
	{
		for (Object object : enumConstants)
		{
			Field strategyType = object.getClass().getSuperclass().getDeclaredField("mStateStrategyType");
			strategyType.setAccessible(true);
			Class<? extends StateStrategy> strategyClass = (Class<? extends StateStrategy>) strategyType.get(object);
			Class<? extends StateStrategy> resultClass = result.get(object.toString());
			assertTrue(String.format("Strategy for field %s should be %s but it has %s", object.toString(), resultClass.getName(), strategyClass.getName()), strategyClass.equals(resultClass));
		}
	}

}
