package com.omegar.mvp.viewstate.strategy;

import java.util.List;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.viewstate.ViewCommand;

/**
 * Command will be added to end of commands queue.
 *
 * This strategy used by default.
 *
 * Date: 17.12.2015
 * Time: 11:29
 *
 * @author Yuri Shmakov
 */
public class AddToEndStrategy implements StateStrategy {
	@Override
	public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
		currentState.add(incomingCommand);
	}

	@Override
	public <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
		// pass
	}
}
