package com.arellomobile.mvp;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Date: 19-Dec-15
 * Time: 13:25
 *
 * @author Alexander Blinov
 */
public class MvpFragment extends Fragment
{
	private MvpDelegate<? extends MvpFragment> mMvpDelegate;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getMvpDelegate().onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		getMvpDelegate().onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		getMvpDelegate().onSaveInstanceState(outState);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		getMvpDelegate().onStart();

	}

	@Override
	public void onStop()
	{
		super.onStop();

		getMvpDelegate().onStop();
	}

	/**
	 * @return The {@link MvpDelegate} being used by this Fragment.
	 */
	public MvpDelegate getMvpDelegate()
	{
		if (mMvpDelegate == null)
		{
			mMvpDelegate = new MvpDelegate<>(this);
		}
		return mMvpDelegate;
	}
}