package com.arellomobile.mvp.viewstate.strategy;

import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;

/**
 * Command will be saved in commands queue. And this command will be removed after first execution.
 *
 * Date: 24.11.2016
 * Time: 11:48
 *
 * @author Yuri Shmakov
 */

public class OneExecutionStateStrategy implements StateStrategy {
	@Override
	public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
		currentState.add(incomingCommand);
	}

	@Override
	public <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
		currentState.remove(incomingCommand);
	}
}
