package com.arellomobile.mvp;

import java.util.HashMap;
import java.util.Map;

import com.arellomobile.mvp.presenter.PresenterType;

/**
 * Date: 17-Dec-15
 * Time: 16:05
 *
 * @author Alexander Blinov
 */
public class PresenterStore {
	private Map<Class<? extends MvpPresenter>, Map<String, MvpPresenter>> mGlobalPresenters = new HashMap<>();
	private Map<Class<? extends MvpPresenter>, Map<String, MvpPresenter>> mWeakPresenters = new WeakValueHashMap<>();

	/**
	 * @param type     Type is presenter local, global or weak
	 * @param tag      Object to store presenter
	 * @param instance Instance of MvpPresenter implementation to store
	 * @param <T>      type of presenter
	 */
	public <T extends MvpPresenter> void add(PresenterType type, String tag, Class<? extends MvpPresenter> clazz, T instance) {
		Map<String, MvpPresenter> mvpPresenterMap;

		final Map<Class<? extends MvpPresenter>, Map<String, MvpPresenter>> presenters = getPresenters(type);

		if (presenters.containsKey(clazz)) {
			mvpPresenterMap = presenters.get(clazz);
		} else {
			mvpPresenterMap = createPresentersStore(type);
			presenters.put(clazz, mvpPresenterMap);
		}

		if (mvpPresenterMap.containsKey(tag)) {
			throw new IllegalStateException("mvp multiple presenters map already contains tag");
		}

		mvpPresenterMap.put(tag, instance);
	}

	public MvpPresenter get(PresenterType type, String tag, Class<? extends MvpPresenter> clazz) {
		Map<String, MvpPresenter> tagMvpPresenterMap = getPresenters(type).get(clazz);

		if (tagMvpPresenterMap == null) {
			//TODO add builder
			return null;
		}

		//TODO add builder if tagMvpPresenterMap.getPresenterFactory(tag) == null

		return tagMvpPresenterMap.get(tag);
	}


	public MvpPresenter remove(PresenterType type, String tag, Class<? extends MvpPresenter> clazz) {
		Map<String, MvpPresenter> tagMvpPresenterMap = getPresenters(type).get(clazz);

		if (tagMvpPresenterMap == null) {
			return null;
		}

		return tagMvpPresenterMap.remove(tag);
	}

	protected Map<Class<? extends MvpPresenter>, Map<String, MvpPresenter>> getPresenters(PresenterType type) {
		if (type == PresenterType.WEAK) {
			return mWeakPresenters;
		}

		return mGlobalPresenters;
	}

	protected Map<String, MvpPresenter> createPresentersStore(PresenterType type) {
		if (type == PresenterType.WEAK) {
			return new WeakValueHashMap<>();
		}

		return new HashMap<>();
	}
}