package com.arellomobile.mvp;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 17-Dec-15
 * Time: 16:05
 *
 * @author Alexander Blinov
 */
public class PresenterStore
{
	private Map<Class<? extends MvpPresenter>, Map<String, MvpPresenter>> mMultipleMvpPresenters = new HashMap<>();

	/**
	 * @param tag      Object to store presenter
	 * @param instance Instance of MvpPresenter implementation to store
	 * @param <T>      type of presenter
	 */
	public <T extends MvpPresenter> void add(String tag, T instance)
	{
		Map<String, MvpPresenter> mvpPresenterMap;

		if (mMultipleMvpPresenters.containsKey(instance.getClass()))
		{
			mvpPresenterMap = mMultipleMvpPresenters.get(instance.getClass());
		}
		else
		{
			mvpPresenterMap = new HashMap<>();
			mMultipleMvpPresenters.put(instance.getClass(), mvpPresenterMap);
		}

		if (mvpPresenterMap.containsKey(tag))
		{
			throw new IllegalStateException("mvp multiple presenters map already contains tag");
		}

		mvpPresenterMap.put(tag, instance);
	}

	public MvpPresenter get(String tag, Class<? extends MvpPresenter> clazz)
	{
		Map<String, MvpPresenter> tagMvpPresenterMap = mMultipleMvpPresenters.get(clazz);

		if (tagMvpPresenterMap == null)
		{
			//TODO add builder
			return null;
		}

		//TODO add builder if tagMvpPresenterMap.getPresenterFactory(tag) == null

		return tagMvpPresenterMap.get(tag);
	}


	public MvpPresenter remove(String tag, Class<? extends MvpPresenter> clazz)
	{
		Map<String, MvpPresenter> tagMvpPresenterMap = mMultipleMvpPresenters.get(clazz);

		if (tagMvpPresenterMap == null)
		{
			return null;
		}

		return tagMvpPresenterMap.remove(tag);
	}
}