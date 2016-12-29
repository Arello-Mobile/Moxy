package com.arellomobile.mvp.inheritance_test.resources;

import com.arellomobile.mvp.MvpDelegate;

/**
 * Date: 30.12.2016
 * Time: 00:09
 *
 * @author Yuri Shmakov
 */
public class ViewWithoutInject {
	public MvpDelegate<? extends ViewWithoutInject> delegate;
}
