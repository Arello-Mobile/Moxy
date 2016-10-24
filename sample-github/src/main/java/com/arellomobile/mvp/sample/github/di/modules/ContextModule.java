package com.arellomobile.mvp.sample.github.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Date: 8/18/2016
 * Time: 14:50
 *
 * @author Artur Artikov
 */
@Module
public class ContextModule {
	private Context mContext;

	public ContextModule(Context context) {
		mContext = context;
	}

	@Provides
	@Singleton
	public Context provideContext() {
		return mContext;
	}
}
