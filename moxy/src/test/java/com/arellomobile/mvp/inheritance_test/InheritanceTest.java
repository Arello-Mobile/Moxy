package com.arellomobile.mvp.inheritance_test;

import android.os.Bundle;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.inheritance_test.resources.ChildViewWithoutInject;
import com.arellomobile.mvp.inheritance_test.resources.SuperViewWithInject;
import com.arellomobile.mvp.inheritance_test.resources.ViewWithoutInject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Date: 30.12.2016
 * Time: 00:29
 *
 * @author Yuri Shmakov
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class InheritanceTest {

	@Test
	public void testWithoutInject() {
		ViewWithoutInject view = new ViewWithoutInject();

		view.delegate = new MvpDelegate<>(view);

		view.delegate.onCreate(new Bundle());
	}

	@Test
	public void testInjectInInherited() {
		SuperViewWithInject view = new SuperViewWithInject();

		view.delegate = new MvpDelegate<>(view);

		view.delegate.onCreate(new Bundle());

		Assert.assertNotNull(view.presenter);
	}

	@Test
	public void testInjectOnlyInSuper() {
		ChildViewWithoutInject view = new ChildViewWithoutInject();

		view.delegate = new MvpDelegate<>(view);

		view.delegate.onCreate();

		Assert.assertNotNull(view.presenter);
	}
}
