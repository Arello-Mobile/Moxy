package com.arellomobile.mvp.compiler.view;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.factory.MockPresenterFactory;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.InjectViewStatePresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import params.EmptyParams;

public class EmptyParamsView implements MvpView, EmptyParams {
	@InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
	public MvpPresenter<MvpView> mInjectViewStatePresenter;
}