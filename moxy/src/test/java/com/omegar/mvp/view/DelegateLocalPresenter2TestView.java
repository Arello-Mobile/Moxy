package com.omegar.mvp.view;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.InjectViewStatePresenter;
import com.omegar.mvp.presenter.PresenterType;

public class DelegateLocalPresenter2TestView extends CounterTestView {
	@InjectPresenter(type = PresenterType.LOCAL)
	public InjectViewStatePresenter mInjectViewStatePresenter;
}