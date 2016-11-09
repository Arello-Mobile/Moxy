package com.arellomobile.mvp.sample.github.mvp.presenters;

import android.content.Context;

import com.arellomobile.mvp.sample.github.BuildConfig;
import com.arellomobile.mvp.sample.github.app.GithubApp;
import com.arellomobile.mvp.sample.github.mvp.common.AuthUtils;
import com.arellomobile.mvp.sample.github.mvp.views.SignOutView$$State;
import com.arellomobile.mvp.sample.github.test.TestComponent;
import com.arellomobile.mvp.sample.github.test.TestComponentRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public final class SignOutPresenterTest {

    @Rule
    public TestComponentRule rule = new TestComponentRule();

    @Mock
    SignOutView$$State signOutViewState;

    private SignOutPresenter signOutPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        signOutPresenter = new SignOutPresenter();
        signOutPresenter.setViewState(signOutViewState);
        AuthUtils.setToken("some token");
    }

    @Test
    public void signout_shouldSingOut() {
        signOutPresenter.signOut();

        verify(signOutViewState).signOut();
        assertEquals("", AuthUtils.getToken());
    }
}