package com.arellomobile.mvp.sample.github.mvp.presenters;

import java.util.List;

import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoryView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

/**
 * Date: 27.01.2016
 * Time: 21:12
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class RepositoryPresenter extends MvpPresenter<RepositoryView> {
	private boolean mIsFirstViewAttached = false;
	private Repository mRepository;
	private List<Integer> mInProgress;
	private List<Integer> mLikedIds;

	public void setRepository(Repository repository) {
		mRepository = repository;

		if (mIsFirstViewAttached) {
			getViewState().showRepository(repository);

			updateLikes(mInProgress, mLikedIds);
		}
	}

	@Override
	protected void onFirstViewAttach() {
		super.onFirstViewAttach();

		mIsFirstViewAttached = true;

		setRepository(mRepository);
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
