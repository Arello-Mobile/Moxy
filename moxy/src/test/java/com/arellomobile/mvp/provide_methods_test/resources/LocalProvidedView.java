package com.arellomobile.mvp.provide_methods_test.resources;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

/**
 * Date: 30.12.2016
 * Time: 12:05
 *
 * @author Yuri Shmakov
 */

public class LocalProvidedView implements TestView {
	@InjectPresenter
	public TestPresenter oneLocalPresenter;
	public TestPresenter oneLocalProvidedPresenter;

	public MvpDelegate<LocalProvidedView> delegate;

	@ProvidePresenter
	TestPresenter provideLocalPresenter() {
		oneLocalProvidedPresenter = new TestPresenter();
		return oneLocalProvidedPresenter;
	}
}
