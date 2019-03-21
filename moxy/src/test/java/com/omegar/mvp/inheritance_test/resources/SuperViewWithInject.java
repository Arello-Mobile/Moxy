package com.omegar.mvp.inheritance_test.resources;

import com.omegar.mvp.presenter.InjectPresenter;

/**
 * Date: 30.12.2016
 * Time: 00:11
 *
 * @author Yuri Shmakov
 */
public class SuperViewWithInject extends ViewWithoutInject implements TestView {
	@InjectPresenter
	public TestPresenter presenter;
}
