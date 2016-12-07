package com.arellomobile.mvp;

import com.arellomobile.mvp.viewstate.MvpViewState;

/**
 * Date: 18.12.2015
 * Time: 13:15
 *
 * @author Yuri Shmakov
 */
public abstract class ViewStateProvider {
	/**
	 * <p>Presenter creates view state object by calling this method.</p>
	 *
	 * @return view state class name
	 */
	public abstract MvpViewState getViewState();
}
