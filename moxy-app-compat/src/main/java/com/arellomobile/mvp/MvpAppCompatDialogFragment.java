package com.arellomobile.mvp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * Date: 17-Dec-16
 * Time: 21:55
 *
 * @author Konstantin Tckhovrebov
 */
@SuppressWarnings({"ConstantConditions", "unused"})
public class MvpAppCompatDialogFragment extends AppCompatDialogFragment {

	private boolean mIsStateSaved;
	private MvpDelegate<? extends MvpAppCompatDialogFragment> mMvpDelegate;

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

		//We leave the screen and respectively all fragments will be destroyed
		if (getActivity().isFinishing()) {
			getMvpDelegate().onDestroy();
			return;
		}

		// When we rotate device isRemoving() return true for fragment placed in backstack
		// http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
		if (mIsStateSaved) {
			mIsStateSaved = false;
			return;
		}

		// See https://github.com/Arello-Mobile/Moxy/issues/24
		boolean anyParentIsRemoving = false;
		Fragment parent = getParentFragment();
		while (!anyParentIsRemoving && parent != null) {
			anyParentIsRemoving = parent.isRemoving();
			parent = parent.getParentFragment();
		}

		if (isRemoving() || anyParentIsRemoving) {
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