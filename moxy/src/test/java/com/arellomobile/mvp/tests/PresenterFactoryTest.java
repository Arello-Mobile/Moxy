package com.arellomobile.mvp.tests;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.view.DelegateLocalPresenter2TestView;
import com.arellomobile.mvp.view.TestView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Date: 09.02.2016
 * Time: 12:37
 *
 * @author Savin Mikhail
 */
public class PresenterFactoryTest
{
	DelegateLocalPresenter2TestView mDelegateLocalPresenterTestView = new DelegateLocalPresenter2TestView();
	DelegateLocalPresenter2TestView mDelegateLocalPresenter2TestView = new DelegateLocalPresenter2TestView();

	MvpDelegate<? extends TestView> mTestViewMvpDelegate = new MvpDelegate<>(mDelegateLocalPresenterTestView);
	MvpDelegate<? extends TestView> mTestViewMvpDelegate2 = new MvpDelegate<>(mDelegateLocalPresenter2TestView);


	@Before
	public void setup()
	{
		mTestViewMvpDelegate.onCreate(null);
		mTestViewMvpDelegate.onStart();

		mTestViewMvpDelegate2.onCreate(null);
		mTestViewMvpDelegate2.onStart();
	}

	@After
	public void reset()
	{
		mTestViewMvpDelegate.onStop();
		mTestViewMvpDelegate.onDestroy();

		mTestViewMvpDelegate2.onStop();
		mTestViewMvpDelegate2.onDestroy();
	}

	@Test
	public void checkLocalPresenters()
	{
		assertEquals("Local Presenters with same presenterId was not equal", mDelegateLocalPresenterTestView.mInjectViewStatePresenter.hashCode(), mDelegateLocalPresenter2TestView.mInjectViewStatePresenter.hashCode());
	}
}
