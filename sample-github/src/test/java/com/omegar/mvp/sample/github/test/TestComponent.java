package com.omegar.mvp.sample.github.test;

import android.content.Context;

import com.omegar.mvp.sample.github.di.AppComponent;
import com.omegar.mvp.sample.github.mvp.GithubService;
import com.omegar.mvp.sample.github.mvp.presenters.RepositoriesPresenter;
import com.omegar.mvp.sample.github.mvp.presenters.RepositoryLikesPresenter;
import com.omegar.mvp.sample.github.mvp.presenters.SignInPresenter;
import com.squareup.otto.Bus;

import org.robolectric.RuntimeEnvironment;

public class TestComponent implements AppComponent {
	@Override
	public Context getContext() {
		return RuntimeEnvironment.application;
	}

	@Override
	public GithubService getAuthService() {
		return null;
	}

	@Override
	public Bus getBus() {
		return null;
	}

	@Override
	public void inject(SignInPresenter presenter) {

	}

	@Override
	public void inject(RepositoriesPresenter repositoriesPresenter) {

	}

	@Override
	public void inject(RepositoryLikesPresenter repositoryLikesPresenter) {

	}
}
