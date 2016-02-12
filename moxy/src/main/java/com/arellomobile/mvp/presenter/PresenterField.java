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

	void setValue(com.arellomobile.mvp.MvpPresenter presenter);

	Class<? extends com.arellomobile.mvp.MvpPresenter<View>> getPresenterClass();

	Class<? extends PresenterFactory<?, ?>> getFactory();

	String getPresenterId();

	Class<? extends ParamsHolder<?>> getParamsHolderClass();
}
