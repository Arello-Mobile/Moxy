package com.arellomobile.mvp.viewstate;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

/**
 * Date: 16-Dec-15
 * Time: 16:59
 *
 * @author Alexander Blinov
 */
public interface ViewCommand<View extends MvpView>
{
	void apply(View view, Object params);

	Class<? extends StateStrategy> getStrategyType();

	String getTag();
}
