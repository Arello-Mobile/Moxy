package com.omegar.mvp.sample.github.mvp.views;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.viewstate.strategy.SingleStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by senneco on 23.10.2016
 */
public interface RepositoryLikeView extends MvpView {
	@StateStrategyType(SingleStateStrategy.class)
	void setState(boolean isInProgress, boolean isLiked);
}
