package com.arellomobile.mvp.sample.github.mvp.presenters;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.util.Base64;

import com.arellomobile.mvp.sample.github.R;
import com.arellomobile.mvp.sample.github.di.AppComponent;
import com.arellomobile.mvp.sample.github.mvp.GithubService;
import com.arellomobile.mvp.sample.github.mvp.common.AuthUtils;
import com.arellomobile.mvp.sample.github.mvp.models.User;
import com.arellomobile.mvp.sample.github.mvp.views.SignInView$$State;
import com.arellomobile.mvp.sample.github.test.GithubSampleTestRunner;
import com.arellomobile.mvp.sample.github.test.TestComponent;
import com.arellomobile.mvp.sample.github.test.TestComponentRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;


import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GithubSampleTestRunner.class)
public final class SignInPresenterTest {

	@Mock
	GithubService githubService;
	@Rule
	public TestComponentRule testComponentRule = new TestComponentRule(testAppComponent());
	@Mock
	SignInView$$State signInViewState;

	private SignInPresenter presenter;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		presenter = new SignInPresenter();
		presenter.setViewState(signInViewState);

		RxJavaPlugins.getInstance().reset();
		RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
			@Override
			public Scheduler getIOScheduler() {
				return Schedulers.immediate();
			}
		});
		RxAndroidPlugins.getInstance().reset();
		RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
			@Override
			public Scheduler getMainThreadScheduler() {
				return Schedulers.immediate();
			}
		});
	}

	@Test
	public void signin_shouldSignSuccessfull() {
		String token = token();
		when(githubService.signIn(token)).thenReturn(Observable.just(new User()));

		presenter.signIn(email(), password());

		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertEquals(token, AuthUtils.getToken());
		isSignInAndHideShowProgressCalled();
		verify(signInViewState).successSignIn();
	}

	@Test
	public void signin_shouldShowError() {
		when(githubService.signIn(token())).thenReturn(Observable.error(new Throwable()));

		presenter.signIn(email(), password());

		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertEquals("", "");

		isSignInAndHideShowProgressCalled();
		verify(signInViewState).failedSignIn(anyString());
	}

	@Test
	public void signin_shouldShowPasswordAndEmailEmptyErros() {
		presenter.signIn(null, null);
		verify(signInViewState).showFormError(R.string.error_field_required, R.string.error_invalid_password);
	}

	@Test
	public void signin_shouldOnErrorCancel() {
		presenter.onErrorCancel();
		verify(signInViewState).hideError();
	}

	private void isSignInAndHideShowProgressCalled() {
		verify(signInViewState).hideFormError();
		verify(signInViewState).startSignIn();
		verify(signInViewState).finishSignIn();
		verify(githubService).signIn(token());
	}

	private AppComponent testAppComponent() {
		return new TestComponent() {
			@Override
			public Context getContext() {
				return RuntimeEnvironment.application;
			}

			@Override
			public void inject(SignInPresenter presenter) {
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