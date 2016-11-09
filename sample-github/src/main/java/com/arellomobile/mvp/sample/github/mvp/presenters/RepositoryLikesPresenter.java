package com.arellomobile.mvp.sample.github.mvp.presenters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.arellomobile.mvp.sample.github.app.GithubApp;
import com.arellomobile.mvp.sample.github.mvp.common.RxUtils;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoryLikesView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import rx.Observable;

/**
 * Date: 26.01.2016
 * Time: 16:32
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class RepositoryLikesPresenter extends MvpPresenter<RepositoryLikesView> {
	public static final String TAG = "RepositoryLikesPresenter";

	@Inject
	Bus mBus;

	private List<Integer> mInProgress = new ArrayList<>();
	private List<Integer> mLikedIds = new ArrayList<>();

	public RepositoryLikesPresenter() {
		GithubApp.getAppComponent().inject(this);

		mBus.register(this);
	}

	public void toggleLike(int id) {
		if (mInProgress.contains(id)) {
			return;
		}

		mInProgress.add(id);

		getViewState().updateLikes(mInProgress, mLikedIds);

		final Observable<Boolean> toggleObservable = Observable.create(subscriber -> {
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			subscriber.onNext(!mLikedIds.contains(id));
		});

		toggleObservable
				.subscribe(isLiked -> {
					onComplete(id, isLiked);
				}, throwable -> {
					onFail(id);
				});
	}

	private void onComplete(int id, Boolean isLiked) {
		if (!mInProgress.contains(id)) {
			return;
		}

		mInProgress.remove(Integer.valueOf(id));
		if (isLiked) {
			mLikedIds.add(id);
		} else {
			mLikedIds.remove(Integer.valueOf(id));
		}

		getViewState().updateLikes(mInProgress, mLikedIds);
	}

	private void onFail(int id) {
		if (!mInProgress.contains(id)) {
			return;
		}

		mInProgress.remove(Integer.valueOf(id));
		getViewState().updateLikes(mInProgress, mLikedIds);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		mBus.unregister(this);
	}
}
