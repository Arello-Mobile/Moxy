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
	private Map<Key, MvpPresenter> mPresenters = new HashMap<>();

	/**
	 * Add presenter to storage
	 *
	 * @param type     Type is presenter local, global or weak
	 * @param tag      Tag of presenter
	 * @param clazz    Presenter class
	 * @param instance Instance of MvpPresenter implementation to store
	 * @param <T>      Type of presenter
	 */
	public <T extends MvpPresenter> void add(PresenterType type, String tag, Class<? extends MvpPresenter> clazz, T instance) {
		Key key = new Key(type, clazz, tag);
		mPresenters.put(key, instance);
	}

	/**
	 * Get presenter on existing params
	 *
	 * @param type     Type is presenter local, global or weak
	 * @param tag      Tag of presenter
	 * @param clazz    Presenter class
	 * @return         Presenter if it's exists. Null otherwise (if it's no exists)
	 */
	public MvpPresenter get(PresenterType type, String tag, Class<? extends MvpPresenter> clazz) {
		Key key = new Key(type, clazz, tag);
		return mPresenters.get(key);
	}

	/**
	 * Remove presenter from store.
	 *
	 * @param type     Type is presenter local, global or weak
	 * @param tag      Tag of presenter
	 * @param clazz    Presenter class
	 * @return         Presenter which was removed
	 */
	public MvpPresenter remove(PresenterType type, String tag, Class<? extends MvpPresenter> clazz) {
		Key key = new Key(type, clazz, tag);
		return mPresenters.remove(key);
	}

	private static class Key {
		PresenterType mPresenterType;
		Class<? extends MvpPresenter> mPresenterClass;
		String mPresenterTag;

		Key(PresenterType presenterType, Class<? extends MvpPresenter> presenterClass, String presenterTag) {
			mPresenterType = presenterType;
			mPresenterClass = presenterClass;
			mPresenterTag = presenterTag;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			Key key = (Key) o;

			if (mPresenterType != key.mPresenterType) {
				return false;
			}
			if (!mPresenterClass.equals(key.mPresenterClass)) {
				return false;
			}
			return mPresenterTag.equals(key.mPresenterTag);
		}

		@Override
		public int hashCode() {
			int result = mPresenterType.hashCode();
			result = 31 * result + mPresenterClass.hashCode();
			result = 31 * result + mPresenterTag.hashCode();
			return result;
		}
	}
}