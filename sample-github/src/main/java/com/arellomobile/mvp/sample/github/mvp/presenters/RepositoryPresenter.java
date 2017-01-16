package com.arellomobile.mvp.sample.github.mvp.presenters;

import java.util.List;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoryView;

/**
 * Date: 27.01.2016
 * Time: 21:12
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class RepositoryPresenter extends MvpPresenter<RepositoryView> {

	private Repository mRepository;
	private List<Integer> mInProgress;
	private List<Integer> mLikedIds;

	public RepositoryPresenter(Repository repository) {
		super();

		mRepository = repository;
	}

	@Override
	protected void onFirstViewAttach() {
		super.onFirstViewAttach();

		getViewState().showRepository(mRepository);

		updateLikes(mInProgress, mLikedIds);
	}

	public void updateLikes(List<Integer> inProgress, List<Integer> likedIds) {
		mInProgress = inProgress;
		mLikedIds = likedIds;

		if (mRepository == null || mInProgress == null || mLikedIds == null) {
			return;
		}

		boolean isInProgress = inProgress.contains(mRepository.getId());
		boolean isLiked = likedIds.contains(mRepository.getId());

		getViewState().updateLike(isInProgress, isLiked);
	}
}
