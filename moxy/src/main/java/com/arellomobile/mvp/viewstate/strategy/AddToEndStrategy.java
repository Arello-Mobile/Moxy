package com.arellomobile.mvp.viewstate.strategy;

import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.Pair;
import com.arellomobile.mvp.viewstate.ViewCommand;

/**
 * Date: 17.12.2015
 * Time: 11:29
 *
 * @author Yuri Shmakov
 */
public class AddToEndStrategy implements StateStrategy
{
	@Override
	public <View extends MvpView> void beforeApply(List<Pair<ViewCommand<View>, Object>> currentState, Pair<ViewCommand<View>, Object> incomingState)
	{
		currentState.add(incomingState);
	}

	@Override
	public <View extends MvpView> void afterApply(List<Pair<ViewCommand<View>, Object>> currentState, Pair<ViewCommand<View>, Object> incomingState)
	{
		// pass
	}
}
