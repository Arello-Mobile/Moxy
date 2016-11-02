package com.arellomobile.mvp;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;

/**
 * Date: 19-Dec-15
 * Time: 13:25
 *
 * @author Alexander Blinov
 * @author Yuri Shmakov
 * @author Konstantin Tckhovrebov
 */
public class MvpFragment extends Fragment {
	private MvpDelegate<? extends MvpFragment> mMvpDelegate;

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

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		getMvpDelegate().onSaveInstanceState(outState);
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
