package com.omegar.mvp.provide_methods_test.resources;

import com.omegar.mvp.MvpDelegate;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.PresenterType;
import com.omegar.mvp.presenter.ProvidePresenter;
import com.omegar.mvp.presenter.ProvidePresenterTag;

/**
 * Date: 30.12.2016
 * Time: 12:05
 *
 * @author Yuri Shmakov
 */

public class TwoWeakWithDiffPresenterIdView implements TestView {
	@InjectPresenter(type = PresenterType.WEAK, presenterId = "oneWeakPresenter")
	public TestPresenter oneWeakPresenter;

	@InjectPresenter(type = PresenterType.WEAK, presenterId = "secondWeakPresenter")
	public TestPresenter secondWeakPresenter;

	public MvpDelegate<TwoWeakWithDiffPresenterIdView> delegate;

	@ProvidePresenterTag(presenterClass = TestPresenter.class, type = PresenterType.WEAK, presenterId = "oneWeakPresenter")
	public String provideOnePresenterTag() {
		return "oneWeakPresenterTag";
	}

	@ProvidePresenterTag(presenterClass = TestPresenter.class, type = PresenterType.WEAK, presenterId = "secondWeakPresenter")
	public String provideSecondPresenterTag() {
		return "secondWeakPresenterTag";
	}

	@ProvidePresenter(type = PresenterType.WEAK, presenterId = "oneWeakPresenter")
	public TestPresenter provideOneTestPresenter() {
		return new TestPresenter();
	}

	@ProvidePresenter(type = PresenterType.WEAK, presenterId = "secondWeakPresenter")
	public TestPresenter provideSecondTestPresenter() {
		return new TestPresenter();
	}
}
