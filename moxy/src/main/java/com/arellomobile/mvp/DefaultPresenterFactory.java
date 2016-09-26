package com.arellomobile.mvp;

import android.text.TextUtils;

/**
 * Date: 25-Dec-15
 * Time: 12:00
 *
 * @author Alexander Blinov
 */
public class DefaultPresenterFactory implements PresenterFactory<MvpPresenter<?>, DefaultPresenterFactory.Params> {
	@Override
	public MvpPresenter createPresenter(Class<MvpPresenter<?>> presenterClazz, Params params) {
		//noinspection TryWithIdenticalCatches
		try {
			return presenterClazz.newInstance();
		} catch (InstantiationException e) {
			// pass
		} catch (IllegalAccessException e) {
			// pass
		}

		throw new IllegalStateException("Unable to instantiate " + presenterClazz + ": " +
		                                "make sure class name exists, " +
		                                "is public, and " +
		                                "has an empty constructor that is public");
	}

	@Override
	public String createTag(Class<MvpPresenter<?>> presenterClazz, Params params) {
		if (!TextUtils.isEmpty(params.defaultTag)) {
			return params.defaultTag;
		}

		return params.delegateTag + "$" + params.fieldName;
	}

	static class Params {
		private String delegateTag;
		private String fieldName;
		private String defaultTag;

		Params(String delegateTag, String fieldName, String defaultTag) {
			this.delegateTag = delegateTag;
			this.fieldName = fieldName;
			this.defaultTag = defaultTag;
		}
	}
}
