package com.arellomobile.mvp;

import java.util.Set;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;

/**
 * Date: 19.12.2015
 * Time: 14:54
 *
 * @author Yuri Shmakov
 */
public final class DefaultViewState extends MvpViewState<MvpView> {

	@Override
	protected void restoreState(MvpView view, Set<ViewCommand<MvpView>> currentState) {
		// Stub!
	}
}
