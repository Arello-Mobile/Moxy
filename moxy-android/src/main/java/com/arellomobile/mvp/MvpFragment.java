package com.arellomobile.mvp;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

/**
 * Date: 19-Dec-15
 * Time: 13:25
 *
 * @author Yuri Shmakov
 * @author Alexander Blinov
 * @author Konstantin Tckhovrebov
 */
@SuppressWarnings("ConstantConditions")
public class MvpFragment extends Fragment {

	private boolean mIsStateSaved;
	private MvpDelegate<? extends MvpFragment> mMvpDelegate;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getMvpDelegate().onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();

		mIsStateSaved = false;

		getMvpDelegate().onAttach();
	}

	public void onResume() {
		super.onResume();

		mIsStateSaved = false;

		getMvpDelegate().onAttach();
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		mIsStateSaved = true;

		getMvpDelegate().onSaveInstanceState(outState);
		getMvpDelegate().onDetach();
	}

	@Override
	public void onStop() {
		super.onStop();

		getMvpDelegate().onDetach();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		getMvpDelegate().onDetach();
		getMvpDelegate().onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mIsStateSaved) {
			mIsStateSaved = false;
			return;
		}

		boolean anyParentIsRemoving = false;

		if (Build.VERSION.SDK_INT >= 17) {
			Fragment parent = getParentFragment();
			while (!anyParentIsRemoving && parent != null) {
				anyParentIsRemoving = parent.isRemoving();
				parent = parent.getParentFragment();
			}
		}

		if (isRemoving() || anyParentIsRemoving || getActivity().isFinishing()) {
			getMvpDelegate().onDestroy();
		}
	}

	/**
	 * @return The {@link MvpDelegate} being used by this Fragment.
	 */
	public MvpDelegate getMvpDelegate() {
		if (mMvpDelegate == null) {
			mMvpDelegate = new MvpDelegate<>(this);
		}

		return mMvpDelegate;
	}
}
