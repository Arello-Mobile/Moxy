package com.arellomobile.mvp.sample.github.test;

import java.lang.reflect.Method;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.sample.github.BuildConfig;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

public class GithubSampleTestRunner extends RobolectricTestRunner {

	private static final int SDK_EMULATE_LEVEL = 23;

	public GithubSampleTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	public Config getConfig(@NonNull Method method) {
		final Config defaultConfig = super.getConfig(method);
		return new Config.Implementation(
				new int[]{SDK_EMULATE_LEVEL},
				defaultConfig.manifest(),
				defaultConfig.qualifiers(),
				defaultConfig.packageName(),
				defaultConfig.resourceDir(),
				defaultConfig.assetDir(),
				defaultConfig.shadows(),
				defaultConfig.instrumentedPackages(),
				defaultConfig.application(),
				defaultConfig.libraries(),
				defaultConfig.constants() == Void.class ? BuildConfig.class : defaultConfig.constants()
		);
	}
}
