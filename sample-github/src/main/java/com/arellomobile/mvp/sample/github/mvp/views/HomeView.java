package com.arellomobile.mvp.sample.github.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 27.01.2016
 * Time: 20:00
 *
 * @author Yuri Shmakov
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface HomeView extends MvpView {
	void showDetailsContainer();

	void setSelection(int position);

	@StateStrategyType(OneExecutionStateStrategy.class)
	void showDetails(int position, Repository repository);
}
