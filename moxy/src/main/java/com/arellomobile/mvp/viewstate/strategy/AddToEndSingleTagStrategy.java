package com.arellomobile.mvp.viewstate.strategy;

import java.util.Iterator;
import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;

/**
 * Command will be added to end of commands queue. If commands queue contains same tag command, then existing command will be removed.
 *
 * Date: 14.04.2017
 * Time: 1:09
 *
 * @author Maxim Pestryakov
 */
public class AddToEndSingleTagStrategy implements StateStrategy {
	@Override
	public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
		Iterator<ViewCommand<View>> iterator = currentState.iterator();

		while (iterator.hasNext()) {
			ViewCommand<View> entry = iterator.next();

			if (entry.getTag().equals(incomingCommand.getTag())) {
				iterator.remove();
				break;
			}
		}

		currentState.add(incomingCommand);
	}

	@Override
	public <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
		// pass
	}
}
