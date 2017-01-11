package com.arellomobile.mvp.sample.github.mvp.presenters;

import com.arellomobile.mvp.sample.github.mvp.common.AuthUtils;
import com.arellomobile.mvp.sample.github.mvp.views.SplashView;
import com.arellomobile.mvp.sample.github.test.GithubSampleTestRunner;
import com.arellomobile.mvp.sample.github.test.TestComponentRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.mockito.Mockito.verify;

@RunWith(GithubSampleTestRunner.class)
public class SplashPresenterTest {

	@Rule
	public TestComponentRule testComponentRule = new TestComponentRule();

	@Mock
	SplashView splashView;

	private SplashPresenter presenter;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		presenter = new SplashPresenter();
		presenter.getAttachedViews().add(splashView);
	}

	@Test
	public void splash_shouldAuthorizedStateFalse() {
		AuthUtils.setToken(null);
		presenter.attachView(splashView);
		verify(splashView).setAuthorized(false);
	}

	@Test
	public void splash_shouldAuthorizedStateTrue() {
		AuthUtils.setToken("token");
		presenter.attachView(splashView);
		verify(splashView).setAuthorized(true);
	}

}
