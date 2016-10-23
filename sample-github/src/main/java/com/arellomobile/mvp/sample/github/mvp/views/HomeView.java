package com.arellomobile.mvp.sample.github.mvp.views;

import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 27.01.2016
 * Time: 20:00
 *
 * @author Yuri Shmakov
 */
public interface HomeView extends MvpView {
	@StateStrategyType(SkipStrategy.class)
	void showDetails(int position, Repository repository);

	void showDetailsContainer();
}
