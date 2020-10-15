package com.omegar.mvp.sample.github.mvp.presenters;

import com.omegar.mvp.sample.github.mvp.models.Repository;
import com.omegar.mvp.sample.github.mvp.views.HomeView;
import com.omegar.mvp.InjectViewState;
import com.omegar.mvp.MvpPresenter;

/**
 * Date: 27.01.2016
 * Time: 19:59
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class HomePresenter extends MvpPresenter<HomeView> {
	public void onRepositorySelection(int position, Repository repository) {
		getViewState().showDetailsContainer();

		getViewState().setSelection(position);

		getViewState().showDetails(position, repository);
	}
}
