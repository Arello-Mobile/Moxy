package com.arellomobile.mvp.provide_methods_test.resources;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;

/**
 * Date: 30.12.2016
 * Time: 12:05
 *
 * @author Yuri Shmakov
 */

public class TwoWeakWithSamePresenterIdView implements TestView {
	@InjectPresenter(type = PresenterType.WEAK, presenterId = "weakPresenter")
	public TestPresenter oneWeakPresenter;

	@InjectPresenter(type = PresenterType.WEAK, presenterId = "weakPresenter")
	public TestPresenter secondWeakPresenter;

	public MvpDelegate<TwoWeakWithSamePresenterIdView> delegate;

	@ProvidePresenterTag(presenterClass = TestPresenter.class, type = PresenterType.WEAK, presenterId = "weakPresenter")
	public String providePresenterTag() {
		return "weakPresenterTag";
	}

	@ProvidePresenter(type = PresenterType.WEAK, presenterId = "weakPresenter")
	public TestPresenter provideTestPresenter() {
		return new TestPresenter();
	}
}
