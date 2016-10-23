package com.arellomobile.mvp.sample.github.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by senneco on 23.10.2016
 */
public interface RepositoryLikeView extends MvpView {
	@StateStrategyType(SingleStateStrategy.class)
	void setState(boolean isInProgress, boolean isLiked);
}
