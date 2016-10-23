package com.arellomobile.mvp.view;

import com.arellomobile.mvp.factory.MockPresenterFactory;
import com.arellomobile.mvp.params.MockParams;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.InjectViewStatePresenter;
import com.arellomobile.mvp.presenter.PresenterType;

public class DelegateGlobalTestView extends CounterTestView implements MockParams {
	public static final String TEST_GLOBAL_PRESENTER = "TestGlobalPresenter";

	@InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.GLOBAL, tag = TEST_GLOBAL_PRESENTER)
	public InjectViewStatePresenter mInjectViewStatePresenter;

	@Override
	public String mockParams(final String presenterId) {
		return presenterId;
	}
}