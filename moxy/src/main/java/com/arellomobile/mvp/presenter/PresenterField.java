package com.arellomobile.mvp.presenter;

import com.arellomobile.mvp.*;

/**
 * Date: 18-Dec-15
 * Time: 17:50
 *
 * @author Alexander Blinov
 */
public interface PresenterField<View extends MvpView>
{
	String getTag();

	PresenterType getPresenterType();

	void setValue(MvpPresenter presenter);

	Class<? extends MvpPresenter<View>> getPresenterClass();

	MvpPresenter<View> getDefaultInstance();

	Class<? extends PresenterFactory<?, ?>> getFactory();

	String getPresenterId();

	Class<? extends ParamsHolder<?>> getParamsHolderClass();
}
