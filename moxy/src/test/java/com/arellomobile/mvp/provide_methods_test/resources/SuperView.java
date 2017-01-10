package com.arellomobile.mvp.provide_methods_test.resources;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

/**
 * Date: 30.12.2016
 * Time: 10:12
 *
 * @author Yuri Shmakov
 */

public class SuperView implements TestView {
	@InjectPresenter
	public TestPresenter oneLocalPresenter;
	public TestPresenter oneLocalProvidedPresenter;

	@InjectPresenter
	public TestPresenter secondLocalPresenter;

	@InjectPresenter(presenterId = "one_global")
	public TestPresenter oneGlobalPresenter;

	@InjectPresenter(presenterId = "second_global")
	public TestPresenter secondGlobalPresenter;

	public MvpDelegate<SuperView> delegate;

	@ProvidePresenter
	public TestPresenter provideLocalPresenter() {
		oneLocalProvidedPresenter = new TestPresenter();
		return oneLocalProvidedPresenter;
	}
}
