package com.arellomobile.mvp.viewstate;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

/**
 * Date: 16-Dec-15
 * Time: 16:59
 *
 * @author Alexander Blinov
 */
public abstract class ViewCommand<View extends MvpView> {
	private final String mTag;
	private final Class<? extends StateStrategy> mStateStrategyType;

	protected ViewCommand(String tag, Class<? extends StateStrategy> stateStrategyType) {
		mTag = tag;
		mStateStrategyType = stateStrategyType;
	}

	public abstract void apply(View view);

	public String getTag() {
		return mTag;
	}

	public Class<? extends StateStrategy> getStrategyType() {
		return mStateStrategyType;
	}
}
