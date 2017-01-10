package com.arellomobile.mvp.provide_methods_test.resources;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;

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
