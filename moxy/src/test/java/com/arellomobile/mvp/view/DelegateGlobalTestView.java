package com.arellomobile.mvp.view;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.InjectViewStatePresenter;
import com.arellomobile.mvp.presenter.PresenterType;

public class DelegateGlobalTestView extends CounterTestView {
	public static final String TEST_GLOBAL_PRESENTER = "TestGlobalPresenter";

	@InjectPresenter(type = PresenterType.GLOBAL, tag = TEST_GLOBAL_PRESENTER)
	public InjectViewStatePresenter mInjectViewStatePresenter;
}