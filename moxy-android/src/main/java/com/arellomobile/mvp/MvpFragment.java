package com.arellomobile.mvp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Date: 19-Dec-15
 * Time: 13:25
 *
 * @author Alexander Blinov
 * @author Yuri Shmakov
 */
public class MvpFragment extends Fragment
{
	private Bundle mTemporaryBundle;// required for view destroy/restore
	private MvpDelegate<? extends MvpFragment> mMvpDelegate;

	public MvpFragment()
	{
		mTemporaryBundle = null;
	}

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getMvpDelegate().onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (mTemporaryBundle != null)
		{
			getMvpDelegate().onCreate(mTemporaryBundle);
			mTemporaryBundle = null;
		}

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void onStart()
	{
		super.onStart();

		getMvpDelegate().onStart();
	}

	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		getMvpDelegate().onSaveInstanceState(outState);
	}

	public void onStop()
	{
		super.onStop();

		getMvpDelegate().onStop();
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();

		mTemporaryBundle = new Bundle();
		getMvpDelegate().onSaveInstanceState(mTemporaryBundle);

		getMvpDelegate().onDestroy();
	}

	public MvpDelegate getMvpDelegate()
	{
		if (mMvpDelegate == null)
		{
			mMvpDelegate = new MvpDelegate<>(this);
		}

		return mMvpDelegate;
	}
}