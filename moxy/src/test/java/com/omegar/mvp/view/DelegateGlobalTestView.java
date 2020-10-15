package com.omegar.mvp.view;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.InjectViewStatePresenter;
import com.omegar.mvp.presenter.PresenterType;

public class DelegateGlobalTestView extends CounterTestView {
	public static final String TEST_GLOBAL_PRESENTER = "TestGlobalPresenter";

	@InjectPresenter(type = PresenterType.GLOBAL, tag = TEST_GLOBAL_PRESENTER)
	public InjectViewStatePresenter mInjectViewStatePresenter;
}