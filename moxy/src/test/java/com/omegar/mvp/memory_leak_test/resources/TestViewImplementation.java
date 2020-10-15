package com.omegar.mvp.memory_leak_test.resources;

import com.omegar.mvp.MvpDelegate;
import com.omegar.mvp.presenter.InjectPresenter;

/**
 * Date: 29.12.2016
 * Time: 14:33
 *
 * @author Yuri Shmakov
 */

public class TestViewImplementation implements TestView {
	@InjectPresenter
	public TestPresenter presenter;

	public MvpDelegate<TestViewImplementation> delegate;
}
