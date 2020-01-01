package com.arellomobile.mvp.tests;

import android.os.Bundle;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.view.DelegateLocalPresenterTestView;
import com.arellomobile.mvp.view.TestView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Date: 10.02.2016
 * Time: 13:19
 *
 * @author Savin Mikhail
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class MvpDelegateTest {
	private DelegateLocalPresenterTestView mTestView = new DelegateLocalPresenterTestView();
	private MvpDelegate<? extends TestView> mvpDelegate = new MvpDelegate<>(mTestView);

	@Before
	public void init() {
		mvpDelegate.onCreate(Mockito.mock(Bundle.class));
		mvpDelegate.onAttach();
	}

	@After
	public void reset() {
		mvpDelegate.onDetach();
		mvpDelegate.onDestroy();
	}

	@Test
	public void localPresenterTest() {

	}
}
