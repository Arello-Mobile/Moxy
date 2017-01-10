package com.arellomobile.mvp.provide_methods_test.resources;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

/**
 * Date: 30.12.2016
 * Time: 12:39
 *
 * @author Yuri Shmakov
 */

public class TwoLocalProvidedView implements TestView {

	@InjectPresenter
	public TestPresenter oneLocalPresenter;

	@InjectPresenter
	public TestPresenter secondLocalPresenter;

	public MvpDelegate<TwoLocalProvidedView> delegate;

	@ProvidePresenter
	TestPresenter provideLocalPresenter() {
		return new TestPresenter();
	}
}
