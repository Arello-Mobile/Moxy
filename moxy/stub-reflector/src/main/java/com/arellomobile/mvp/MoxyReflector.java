package com.arellomobile.mvp;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 07.12.2016
 * Time: 16:39
 *
 * @author Yuri Shmakov
 */
class MoxyReflector {
	private static Map<Class<?>, Object> sViewStateProviders;

	static {
		sViewStateProviders = new HashMap<>();
	}

	public static Object getViewState(Class<?> presenterClass) {
		return sViewStateProviders.get(presenterClass);
	}

	public static Object getPresenterBinders(Class delegated) {
		throw new RuntimeException("Stub!");
	}
}
