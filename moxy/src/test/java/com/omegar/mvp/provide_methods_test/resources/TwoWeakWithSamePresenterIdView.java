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
