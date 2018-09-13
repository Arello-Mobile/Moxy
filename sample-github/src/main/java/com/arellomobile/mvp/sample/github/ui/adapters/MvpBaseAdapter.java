package com.arellomobile.mvp.sample.github.ui.adapters;

import android.widget.BaseAdapter;

import com.arellomobile.mvp.MvpDelegate;

/**
 * Date: 26.01.2016
 * Time: 17:26
 *
 * @author Yuri Shmakov
 */
public abstract class MvpBaseAdapter extends BaseAdapter {
	private MvpDelegate<? extends MvpBaseAdapter> mvpDelegate;
	private MvpDelegate<?> parentDelegate;
	private String childId;

	public MvpBaseAdapter(MvpDelegate<?> parentDelegate, String childId) {
		this.parentDelegate = parentDelegate;
		this.childId = childId;

		getMvpDelegate().onCreate();
	}

	public MvpDelegate getMvpDelegate() {
		if (mvpDelegate == null) {
			mvpDelegate = new MvpDelegate<>(this);
			mvpDelegate.setParentDelegate(parentDelegate, childId);

		}
		return mvpDelegate;
	}
}
