package com.arellomobile.mvp.viewstate.strategy;

import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;

/**
 * This strategy will clear current commands queue and then incoming command will be put in.
 *
 * Caution! Be sure that you fully set view to initial state inside this command.
 *
 * Date: 19-Dec-15
 * Time: 14:34
 *
 * @author Alexander Blinov
 */
public class SingleStateStrategy implements StateStrategy {
	@Override
	public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
		currentState.clear();
		currentState.add(incomingCommand);
	}

	@Override
	public <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
		// pass
	}
}
