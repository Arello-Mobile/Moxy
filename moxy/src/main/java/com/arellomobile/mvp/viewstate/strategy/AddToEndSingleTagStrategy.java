package com.arellomobile.mvp.viewstate.strategy;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;

import java.util.Iterator;
import java.util.List;

/**
 * Command will be added to end of commands queue. If commands queue contains same tag, then existing command will be removed.
 *
 * @author Vova Stelmashchuk
 */
public class AddToEndSingleTagStrategy implements StateStrategy {
    @Override
    public <View extends MvpView> void beforeApply(final List<ViewCommand<View>> currentState, final ViewCommand<View> incomingCommand) {
        Iterator<ViewCommand<View>> iterator = currentState.iterator();

        while (iterator.hasNext()) {
            ViewCommand<View> entry = iterator.next();

            if (entry.getTag().equals(incomingCommand.getTag())) {
                iterator.remove();
            }
        }

        currentState.add(incomingCommand);
    }

    @Override
    public <View extends MvpView> void afterApply(final List<ViewCommand<View>> currentState, final ViewCommand<View> incomingCommand) {

    }
}
