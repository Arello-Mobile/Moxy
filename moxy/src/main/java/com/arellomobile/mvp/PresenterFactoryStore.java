package com.arellomobile.mvp;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 23-Dec-15
 * Time: 19:37
 *
 * @author Alexander Blinov
 */
public class PresenterFactoryStore
{
	private static Map<Class<? extends PresenterFactory<?, ?>>, PresenterFactory<?, ?>> sPresenterFactories = new HashMap<>();
	private static Map<Class<? extends ParamsHolder<?>>, ParamsHolder<?>> sParamsHolders = new HashMap<>();

	public PresenterFactory<?, ?> getPresenterFactory(Class<? extends PresenterFactory<?, ?>> clazz)
	{
		if (sPresenterFactories.containsKey(clazz))
		{
			//noinspection unchecked
			return sPresenterFactories.get(clazz);
		}

		PresenterFactory<?, ?> instance;

		try
		{
			instance = clazz.newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException("Unable to instantiate " + clazz + ": " +
					"make sure class name exists, " +
					"is public, and " +
					"has an empty constructor that is public", e);
		}

		sPresenterFactories.put(clazz, instance);

		return instance;
	}

	public ParamsHolder<?> getParamsHolder(Class<? extends ParamsHolder<?>> clazz)
	{
		if (sParamsHolders.containsKey(clazz))
		{
			return sParamsHolders.get(clazz);
		}

		ParamsHolder<?> instance;

		try
		{
			instance = clazz.newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException("Unable to instantiate " + clazz + ": " +
					"make sure class name exists, " +
					"is public, and " +
					"has an empty constructor that is public", e);
		}

		sParamsHolders.put(clazz, instance);

		return instance;
	}
}
