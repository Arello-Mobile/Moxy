package com.arellomobile.mvp.sample.github.di;

import android.content.Context;

import com.arellomobile.mvp.sample.github.di.modules.BusModule;
import com.arellomobile.mvp.sample.github.di.modules.ContextModule;
import com.arellomobile.mvp.sample.github.di.modules.GithubModule;
import com.arellomobile.mvp.sample.github.mvp.GithubService;
import com.arellomobile.mvp.sample.github.mvp.presenters.RepositoriesPresenter;
import com.arellomobile.mvp.sample.github.mvp.presenters.RepositoryLikesPresenter;
import com.arellomobile.mvp.sample.github.mvp.presenters.SignInPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Date: 8/18/2016
 * Time: 14:49
 *
 * @author Artur Artikov
 */
@Singleton
@Component(modules = {ContextModule.class, BusModule.class, GithubModule.class})
public interface AppComponent {
	Context getContext();
	GithubService getAuthService();
	Bus getBus();

	void inject(SignInPresenter presenter);
	void inject(RepositoriesPresenter repositoriesPresenter);
	void inject(RepositoryLikesPresenter repositoryLikesPresenter);
}
