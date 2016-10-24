package com.arellomobile.mvp.sample.github.mvp.views;

import java.util.List;

import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 22.01.2016
 * Time: 14:40
 *
 * @author Yuri Shmakov
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface RepositoriesView extends MvpView {
	void showError(String message);

	void hideError();

	void onStartLoading();

	void onFinishLoading();

	void showRefreshing();

	void hideRefreshing();

	void showListProgress();

	void hideListProgress();

	void setRepositories(List<Repository> repositories, boolean maybeMore);

	@StateStrategyType(AddToEndStrategy.class)
	void addRepositories(List<Repository> repositories, boolean maybeMore);
}
