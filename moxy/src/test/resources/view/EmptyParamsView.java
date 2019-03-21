package com.arellomobile.mvp.compiler.view;

import com.omegar.mvp.MvpPresenter;
import com.omegar.mvp.MvpView;
import com.arellomobile.mvp.factory.MockPresenterFactory;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.PresenterType;

import params.EmptyParams;

public class EmptyParamsView implements MvpView, EmptyParams {
	@InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
	public MvpPresenter<MvpView> mInjectViewStatePresenter;
}