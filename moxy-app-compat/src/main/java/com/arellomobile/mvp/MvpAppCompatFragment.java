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
@SuppressWarnings({"ConstantConditions", "unused"})
public class MvpAppCompatFragment extends Fragment {

	private boolean mIsStateSaved;
	private MvpDelegate<? extends MvpAppCompatFragment> mMvpDelegate;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getMvpDelegate().onCreate(savedInstanceState);
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
		Fragment parent = getParentFragment();
		while (!anyParentIsRemoving && parent != null) {
			anyParentIsRemoving = parent.isRemoving();
			parent = parent.getParentFragment();
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