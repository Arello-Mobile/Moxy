package com.arellomobile.mvp.view;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.InjectViewStatePresenter;
import com.arellomobile.mvp.presenter.PresenterType;

public class DelegateLocalPresenter2TestView extends CounterTestView {
	@InjectPresenter(type = PresenterType.LOCAL)
	public InjectViewStatePresenter mInjectViewStatePresenter;
}