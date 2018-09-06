package com.arellomobile.mvp.sample.github.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.arellomobile.mvp.sample.github.di.AppComponent;
import com.arellomobile.mvp.sample.github.di.DaggerAppComponent;
import com.arellomobile.mvp.sample.github.di.modules.ContextModule;

/**
 * Date: 18.01.2016
 * Time: 11:22
 *
 * @author Yuri Shmakov
 */
public class GithubApp extends Application {
	private static AppComponent appComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		appComponent = DaggerAppComponent.builder()
				.contextModule(new ContextModule(this))
				.build();

	}

	public static AppComponent getAppComponent() {
		return appComponent;
	}


	@VisibleForTesting
	public static void setAppComponent(@NonNull AppComponent appComponent) {
		GithubApp.appComponent = appComponent;
	}
}
