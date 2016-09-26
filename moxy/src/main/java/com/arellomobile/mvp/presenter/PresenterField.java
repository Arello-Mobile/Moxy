package com.arellomobile.mvp.presenter;

import com.arellomobile.mvp.*;

/**
 * Date: 18-Dec-15
 * Time: 17:50
 *
 * @author Alexander Blinov
 */
public abstract class PresenterField<View extends MvpView> {
	protected final String tag;
	protected final PresenterType presenterType;
	protected final Class<? extends PresenterFactory<?, ?>> factory;
	protected final String presenterId;
	protected final Class<? extends ParamsHolder<?>> paramsHolderClass;
	protected final Class<? extends MvpPresenter<?>> presenterClass;

	protected PresenterField(String tag, PresenterType presenterType, Class<? extends PresenterFactory<?, ?>> factory, String presenterId, Class<? extends ParamsHolder<?>> paramsHolderClass, Class<? extends MvpPresenter<?>> presenterClass) {
		this.tag = tag;
		this.presenterType = presenterType;
		this.factory = factory;
		this.presenterId = presenterId;
		this.paramsHolderClass = paramsHolderClass;
		this.presenterClass = presenterClass;
	}

	public abstract void setValue(MvpPresenter presenter);

	public String getTag() {
		return tag;
	}

	public PresenterType getPresenterType() {
		return presenterType;
	}

	public Class<? extends PresenterFactory<?, ?>> getFactory() {
		return factory;
	}

	public String getPresenterId() {
		return presenterId;
	}

	public Class<? extends ParamsHolder<?>> getParamsHolderClass() {
		return paramsHolderClass;
	}

	public Class<? extends MvpPresenter<?>> getPresenterClass() {
		return presenterClass;
	}
}
