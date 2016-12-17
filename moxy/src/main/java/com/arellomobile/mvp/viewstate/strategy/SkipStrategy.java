package com.arellomobile.mvp.viewstate.strategy;

import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;

/**
 * Command will not be put in commands queue
 *
 * Date: 21-Dec-15
 * Time: 17:43
 *
 * @author Alexander Blinov
 */
public class SkipStrategy implements StateStrategy {
	@Override
	public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
		//do nothing to skip
	}

	@Override
	public <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
		// pass
	}
}
