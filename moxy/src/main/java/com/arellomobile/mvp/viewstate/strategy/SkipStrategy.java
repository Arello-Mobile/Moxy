package com.arellomobile.mvp.viewstate.strategy;

import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.Pair;
import com.arellomobile.mvp.viewstate.ViewCommand;

/**
 * Date: 21-Dec-15
 * Time: 17:43
 *
 * @author Alexander Blinov
 */
public class SkipStrategy implements StateStrategy
{
	@Override
	public <View extends MvpView> void beforeApply(List<Pair<ViewCommand<View>, Object>> currentState, Pair<ViewCommand<View>, Object> incomingState)
	{
		//do nothing to skip
	}

	@Override
	public <View extends MvpView> void afterApply(List<Pair<ViewCommand<View>, Object>> currentState, Pair<ViewCommand<View>, Object> incomingState)
	{
		// pass
	}
}
