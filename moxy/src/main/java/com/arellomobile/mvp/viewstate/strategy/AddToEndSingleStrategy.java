package com.arellomobile.mvp.viewstate.strategy;

import java.util.Iterator;
import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.Pair;
import com.arellomobile.mvp.viewstate.ViewCommand;

/**
 * Date: 17.12.2015
 * Time: 11:24
 *
 * @author Yuri Shmakov
 */
public class AddToEndSingleStrategy implements StateStrategy
{
	@Override
	public <View extends MvpView> void beforeApply(List<Pair<ViewCommand<View>, Object>> currentState, Pair<ViewCommand<View>, Object> incomingState)
	{
		Iterator<Pair<ViewCommand<View>, Object>> iterator = currentState.iterator();

		while (iterator.hasNext())
		{
			Pair<ViewCommand<View>, Object> entry = iterator.next();

			if (entry.first == incomingState.first)
			{
				iterator.remove();
				break;
			}
		}

		currentState.add(incomingState);
	}

	@Override
	public <View extends MvpView> void afterApply(List<Pair<ViewCommand<View>, Object>> currentState, Pair<ViewCommand<View>, Object> incomingState)
	{
		// pass
	}
}
