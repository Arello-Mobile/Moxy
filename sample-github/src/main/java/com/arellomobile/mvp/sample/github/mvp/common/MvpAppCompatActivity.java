package com.arellomobile.mvp.sample.github.mvp.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arellomobile.mvp.MvpDelegate;

/**
 * Date: 15.01.2016
 * Time: 19:21
 *
 * @author Yuri Shmakov
 */
public class MvpAppCompatActivity extends AppCompatActivity {
	private MvpDelegate<? extends MvpAppCompatActivity> mMvpDelegate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getMvpDelegate().onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		getMvpDelegate().onDetach();

		if (isFinishing()) {
			getMvpDelegate().onDestroy();
		}
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		getMvpDelegate().onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		super.onStart();

		getMvpDelegate().onAttach();
	}

	/**
	 * @return The {@link MvpDelegate} being used by this Activity.
	 */
	public MvpDelegate getMvpDelegate() {
		if (mMvpDelegate == null) {
			mMvpDelegate = new MvpDelegate<>(this);
		}
		return mMvpDelegate;
	}
}