package com.arellomobile.mvp.view;

import com.arellomobile.mvp.factory.MockPresenterFactory;
import com.arellomobile.mvp.params.MockParams;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.InjectViewStatePresenter;
import com.arellomobile.mvp.presenter.PresenterType;

public class DelegateLocalPresenterTestView extends CounterTestView implements MockParams
{
	@InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
	public InjectViewStatePresenter mInjectViewStatePresenter;

	@Override
	public String mockParams(final String presenterId)
	{
		return presenterId + "" + hashCode();
	}

}