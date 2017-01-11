package com.arellomobile.mvp.sample.github.mvp.presenters;

import com.arellomobile.mvp.sample.github.mvp.common.AuthUtils;
import com.arellomobile.mvp.sample.github.mvp.views.SignOutView$$State;
import com.arellomobile.mvp.sample.github.test.GithubSampleTestRunner;
import com.arellomobile.mvp.sample.github.test.TestComponentRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(GithubSampleTestRunner.class)
public final class SignOutPresenterTest {

	@Rule
	public TestComponentRule testComponentRule = new TestComponentRule();

	@Mock
	SignOutView$$State signOutViewState;

	private SignOutPresenter presenter;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		presenter = new SignOutPresenter();
		presenter.setViewState(signOutViewState);
		AuthUtils.setToken("some token");
	}

	@Test
	public void signout_shouldSingOut() {
		presenter.signOut();

		verify(signOutViewState).signOut();
		assertEquals("", AuthUtils.getToken());
	}
}