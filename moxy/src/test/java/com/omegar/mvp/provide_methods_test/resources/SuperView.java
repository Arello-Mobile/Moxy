package com.omegar.mvp.provide_methods_test.resources;

import com.omegar.mvp.MvpDelegate;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

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
