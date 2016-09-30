package com.arellomobile.mvp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Date: 19-Dec-15
 * Time: 13:25
 *
 * @author Alexander Blinov
 * @author Yuri Shmakov
 * @author Konstantin Tckhovrebov
 */
public class MvpAppCompatFragment extends Fragment {
	private MvpDelegate<? extends MvpAppCompatFragment> mMvpDelegate;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getMvpDelegate().onCreate(savedInstanceState);
	}

	public void onStart() {
		super.onStart();

		getMvpDelegate().onAttach();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		getMvpDelegate().onDetach();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (isRemoving()) {
			getMvpDelegate().onDestroy();
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		getMvpDelegate().onSaveInstanceState(outState);
	}

	public MvpDelegate getMvpDelegate() {
		if (mMvpDelegate == null) {
			mMvpDelegate = new MvpDelegate<>(this);
		}

		return mMvpDelegate;
	}
}