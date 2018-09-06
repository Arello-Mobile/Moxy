package com.arellomobile.mvp.sample.github.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoryLikesView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Date: 26.01.2016
 * Time: 16:32
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class RepositoryLikesPresenter extends BasePresenter<RepositoryLikesView> {
	public static final String TAG = "RepositoryLikesPresenter";

	private List<Integer> inProgress = new ArrayList<>();
	private List<Integer> likedIds = new ArrayList<>();

	public void toggleLike(int id) {
		if (inProgress.contains(id)) {
			return;
		}

		inProgress.add(id);

		getViewState().updateLikes(inProgress, likedIds);

		final Observable<Boolean> toggleObservable = Observable.create(subscriber -> {
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			subscriber.onNext(!likedIds.contains(id));
		});

	 	Subscription subscription = toggleObservable
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(isLiked -> {
					onComplete(id, isLiked);
				}, throwable -> {
					onFail(id);
				});
		unsubscribeOnDestroy(subscription);
	}

	private void onComplete(int id, Boolean isLiked) {
		if (!inProgress.contains(id)) {
			return;
		}

		inProgress.remove(Integer.valueOf(id));
		if (isLiked) {
			likedIds.add(id);
		} else {
			likedIds.remove(Integer.valueOf(id));
		}

		getViewState().updateLikes(inProgress, likedIds);
	}

	private void onFail(int id) {
		if (!inProgress.contains(id)) {
			return;
		}

		inProgress.remove(Integer.valueOf(id));
		getViewState().updateLikes(inProgress, likedIds);
	}
}
