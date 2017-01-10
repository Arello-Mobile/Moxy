package com.arellomobile.mvp.provide_methods_test;

import android.os.Bundle;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.provide_methods_test.resources.LocalProvidedView;
import com.arellomobile.mvp.provide_methods_test.resources.TwoLocalProvidedView;
import com.arellomobile.mvp.provide_methods_test.resources.TwoWeakWithSamePresenterIdView;
import com.arellomobile.mvp.provide_methods_test.resources.WeakProvidedView;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Date: 30.12.2016
 * Time: 11:18
 *
 * @author Yuri Shmakov
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ProvideMethodsTest {

	@Test
	public void testLocalIsProvided() {
		LocalProvidedView view = new LocalProvidedView();

		view.delegate = new MvpDelegate<>(view);
		view.delegate.onCreate(new Bundle());

		Assert.assertNotNull(view.oneLocalPresenter);
		Assert.assertSame(view.oneLocalPresenter, view.oneLocalProvidedPresenter);
	}

	@Test
	public void testTwoLocalUseDifferentProvided() {
		TwoLocalProvidedView view = new TwoLocalProvidedView();

		view.delegate = new MvpDelegate<>(view);
		view.delegate.onCreate(new Bundle());

		Assert.assertNotSame(view.oneLocalPresenter, view.secondLocalPresenter);
	}

	@Test
	public void testWeakPresenterWithHardcodedTag() {
		WeakProvidedView view = new WeakProvidedView();

		view.delegate = new MvpDelegate<>(view);
		view.delegate.onCreate(new Bundle());

		Assert.assertNotNull(view.weakPresenter);
		Assert.assertSame(view.weakPresenter, view.weakProvidedPresenter);
	}

	@Test
	public void testTwoWeakPresenterWithSamePresenterIdTest() {
		TwoWeakWithSamePresenterIdView view = new TwoWeakWithSamePresenterIdView();

		view.delegate = new MvpDelegate<>(view);
		view.delegate.onCreate(new Bundle());

		Assert.assertNotNull(view.oneWeakPresenter);
		Assert.assertNotNull(view.secondWeakPresenter);

		Assert.assertSame(view.oneWeakPresenter, view.secondWeakPresenter);
	}
}
