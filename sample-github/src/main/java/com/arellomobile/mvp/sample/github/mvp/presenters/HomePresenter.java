package com.arellomobile.mvp.sample.github.mvp.presenters;

import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.views.HomeView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

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
