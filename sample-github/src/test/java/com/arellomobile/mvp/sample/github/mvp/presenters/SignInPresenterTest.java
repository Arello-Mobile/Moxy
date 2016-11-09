package com.arellomobile.mvp.sample.github.mvp.presenters;

import android.content.Context;
import android.util.Base64;

import com.arellomobile.mvp.sample.github.BuildConfig;
import com.arellomobile.mvp.sample.github.R;
import com.arellomobile.mvp.sample.github.app.GithubApp;
import com.arellomobile.mvp.sample.github.di.AppComponent;
import com.arellomobile.mvp.sample.github.mvp.GithubService;
import com.arellomobile.mvp.sample.github.mvp.common.AuthUtils;
import com.arellomobile.mvp.sample.github.mvp.models.User;
import com.arellomobile.mvp.sample.github.mvp.views.SignInView$$State;
import com.arellomobile.mvp.sample.github.test.TestComponent;
import com.arellomobile.mvp.sample.github.test.TestComponentRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import rx.Observable;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public final class SignInPresenterTest {

    @Rule
    public TestComponentRule rule = new TestComponentRule(testAppComponent());

    @Mock
    GithubService githubService;

    @Mock
    SignInView$$State signInViewState;

    private SignInPresenter signInPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        signInPresenter = new SignInPresenter();
        signInPresenter.setViewState(signInViewState);
    }

    @Test
    public void signin_shouldSignSuccessfull() {
        String token = token();
        when(githubService.signIn(token)).thenReturn(Observable.just(new User()));

        signInPresenter.signIn(email(), password());

        Assert.assertEquals(token, AuthUtils.getToken());
        isSignInAndHideShowProgressCalled();
        verify(signInViewState).successSignIn();
    }

    @Test
    public void signin_shouldShowError() {
        when(githubService.signIn(token())).thenReturn(Observable.error(new Throwable()));

        signInPresenter.signIn(email(), password());

        Assert.assertEquals("", "");

        isSignInAndHideShowProgressCalled();
        verify(signInViewState).showError(anyString());
    }

    @Test
    public void signin_shouldShowPasswordAndEmailEmptyErros() {
        signInPresenter.signIn(null, null);
        verify(signInViewState).showError(R.string.error_field_required, R.string.error_invalid_password);
    }

    @Test
    public void signin_shouldOnErrorCancel() {
        signInPresenter.onErrorCancel();
        verify(signInViewState).hideError();
    }

    private void isSignInAndHideShowProgressCalled() {
        verify(signInViewState).showError(null, null);
        verify(signInViewState).showProgress();
        verify(signInViewState).hideProgress();
        verify(githubService).signIn(token());
    }

    private AppComponent testAppComponent() {
        return new TestComponent() {
            @Override public Context getContext() {
                return RuntimeEnvironment.application;
            }

            @Override public void inject(SignInPresenter presenter) {
                presenter.mGithubService = githubService;
            }
        };
    }

    private String token() {
        String credentials = String.format("%s:%s", email(), password());
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    private String email() {
        return "test@test.ru";
    }

    private String password() {
        return "password";
    }
}