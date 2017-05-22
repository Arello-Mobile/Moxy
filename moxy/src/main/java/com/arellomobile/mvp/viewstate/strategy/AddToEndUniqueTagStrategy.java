package com.arellomobile.mvp.viewstate.strategy;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;

import java.util.Iterator;
import java.util.List;

/**
 * Command will be added to end of commands queue. If commands queue contains same TAG, then existing command will be removed.
 *
 * @author Aleksey Bogomolov
 */
public class AddToEndUniqueTagStrategy implements StateStrategy {
	@Override
	public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand) {
		Iterator iterator = currentState.iterator();

		while(iterator.hasNext()) {
			ViewCommand entry = (ViewCommand)iterator.next();
			if(entry.getTag().equals(incomingCommand.getTag())) {
				iterator.remove();
				break;
			}
		}

		currentState.add(incomingCommand);
	}

	@Override
	public <View extends MvpView> void afterApply(List<ViewCommand<View>> list, ViewCommand<View> viewCommand) {

	}
}
