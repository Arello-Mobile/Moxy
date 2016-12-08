package com.arellomobile.mvp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 07.12.2016
 * Time: 16:39
 *
 * @author Yuri Shmakov
 */
class MoxyReflector {
	private static Map<Class<?>, Object> sViewStateProviders;
	private static Map<Class<?>, List<Object>> sPresenterBinders;

	static {
		sViewStateProviders = new HashMap<>();
		sPresenterBinders = new HashMap<>();
	}

	public static Object getViewState(Class<?> presenterClass) {
		return sViewStateProviders.get(presenterClass);
	}

	public static List<Object> getPresenterBinders(Class<?> delegated) {
		return sPresenterBinders.get(delegated);
	}
}
