package com.arellomobile.mvp.sample.github.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoryView;

import java.util.List;

/**
 * Date: 27.01.2016
 * Time: 21:12
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class RepositoryPresenter extends MvpPresenter<RepositoryView> {

	private Repository repository;
	private List<Integer> inProgress;
	private List<Integer> likedIds;

	public RepositoryPresenter(Repository repository) {
		super();

		this.repository = repository;
	}

	@Override
	protected void onFirstViewAttach() {
		super.onFirstViewAttach();

		getViewState().showRepository(repository);

		updateLikes(inProgress, likedIds);
	}

	public void updateLikes(List<Integer> inProgress, List<Integer> likedIds) {
		this.inProgress = inProgress;
		this.likedIds = likedIds;

		if (repository == null || this.inProgress == null || this.likedIds == null) {
			return;
		}

		boolean isInProgress = inProgress.contains(repository.getId());
		boolean isLiked = likedIds.contains(repository.getId());

		getViewState().updateLike(isInProgress, isLiked);
	}
}
