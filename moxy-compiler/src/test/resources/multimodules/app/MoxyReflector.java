package com.arellomobile.mvp;

import com.omegar.mvp.ViewStateProvider;

import java.lang.Class;
import java.lang.Object;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import multimodules.app.AppPresenter;
import multimodules.app.AppPresenter$$ViewStateProvider;

public final class MoxyReflector {
	private static Map<Class<?>, Object> sViewStateProviders;

	private static Map<Class<?>, List<Object>> sPresenterBinders;

	private static Map<Class<?>, Object> sStrategies;

	static {
		sViewStateProviders = new HashMap<>();
		sViewStateProviders.put(AppPresenter.class, new AppPresenter$$ViewStateProvider());

		sPresenterBinders = new HashMap<>();

		sStrategies = new HashMap<>();

		sViewStateProviders.putAll(multimodules.lib1.MoxyReflector.getViewStateProviders());
		sPresenterBinders.putAll(multimodules.lib1.MoxyReflector.getPresenterBinders());
		sStrategies.putAll(multimodules.lib1.MoxyReflector.getStrategies());
	}

	public static Object getViewState(Class<?> presenterClass) {
		ViewStateProvider viewStateProvider = (ViewStateProvider) sViewStateProviders.get(presenterClass);
		if (viewStateProvider == null) {
			return null;
		}

		return viewStateProvider.getViewState();
	}

	public static List<Object> getPresenterBinders(Class<?> delegated) {
		return sPresenterBinders.get(delegated);
	}

	public static Object getStrategy(Class<?> strategyClass) {
		return sStrategies.get(strategyClass);
	}
}