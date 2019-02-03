package multimodules.lib1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MoxyReflector {
	private static Map<Class<?>, Object> sViewStateProviders;

	private static Map<Class<?>, List<Object>> sPresenterBinders;

	private static Map<Class<?>, Object> sStrategies;

	static {
		sViewStateProviders = new HashMap<>();
		sViewStateProviders.put(Lib1Presenter.class, new Lib1Presenter$$ViewStateProvider());

		sPresenterBinders = new HashMap<>();

		sStrategies = new HashMap<>();
	}

	public static Map<Class<?>, Object> getViewStateProviders() {
		return sViewStateProviders;
	}

	public static Map<Class<?>, List<Object>> getPresenterBinders() {
		return sPresenterBinders;
	}

	public static Map<Class<?>, Object> getStrategies() {
		return sStrategies;
	}
}
