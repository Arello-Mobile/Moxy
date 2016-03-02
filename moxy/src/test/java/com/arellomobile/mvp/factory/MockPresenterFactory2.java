package com.arellomobile.mvp.factory;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.PresenterFactory;

/**
 * Date: 08.02.2016
 * Time: 17:49
 *
 * @author Savin Mikhail
 */
public class MockPresenterFactory2 implements PresenterFactory<MvpPresenter<?>, String>
{

	@Override
	public MvpPresenter<?> createPresenter(final MvpPresenter<?> defaultInstance, final Class<MvpPresenter<?>> presenterClazz, final String s)
	{
		try
		{
			return presenterClazz.newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return defaultInstance;
	}

	@Override
	public String createTag(final Class<MvpPresenter<?>> aClass, final String s)
	{
		return s;
	}
}
