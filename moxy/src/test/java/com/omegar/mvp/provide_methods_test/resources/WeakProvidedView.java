package com.omegar.mvp.provide_methods_test.resources;

import com.omegar.mvp.MvpDelegate;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.PresenterType;
import com.omegar.mvp.presenter.ProvidePresenter;

/**
 * Date: 30.12.2016
 * Time: 12:05
 *
 * @author Yuri Shmakov
 */

public class WeakProvidedView implements TestView {
	@InjectPresenter(type = PresenterType.WEAK, tag = "weakPresenter")
	public TestPresenter weakPresenter;
	public TestPresenter weakProvidedPresenter;

	public MvpDelegate<WeakProvidedView> delegate;

	@ProvidePresenter(type = PresenterType.WEAK, tag = "weakPresenter")
	public TestPresenter provideWeakPresenter() {
		weakProvidedPresenter = new TestPresenter();
		return weakProvidedPresenter;
	}
}
