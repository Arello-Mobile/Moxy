package com.omegar.mvp.sample.github.mvp.views;

import java.util.List;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 26.01.2016
 * Time: 16:33
 *
 * @author Yuri Shmakov
 */
public interface RepositoryLikesView extends MvpView {
	@StateStrategyType(AddToEndSingleStrategy.class)
	void updateLikes(List<Integer> inProgress, List<Integer> likedIds);
}
