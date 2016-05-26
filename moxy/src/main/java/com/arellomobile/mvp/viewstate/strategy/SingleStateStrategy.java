package com.arellomobile.mvp.viewstate.strategy;

import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;

/**
 * Date: 19-Dec-15
 * Time: 14:34
 *
 * @author Alexander Blinov
 */
public class SingleStateStrategy implements StateStrategy
{
	@Override
	public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingState)
	{
		currentState.clear();
		currentState.add(incomingState);
	}

	@Override
	public <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingState)
	{
		// pass
	}
}
