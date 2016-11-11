package com.arellomobile.mvp.sample.github.mvp.presenters;

import android.text.TextUtils;

import com.arellomobile.mvp.sample.github.mvp.common.AuthUtils;
import com.arellomobile.mvp.sample.github.mvp.views.SplashView;
import com.arellomobile.mvp.MvpPresenter;

import rx.Observable;
import rx.Subscription;

/**
 * Date: 18.01.2016
 * Time: 15:38
 *
 * @author Yuri Shmakov
 */
public class SplashPresenter extends BasePresenter<SplashView> {
	public void checkAuthorized() {
		final Observable<String> getTokenObservable = Observable.create(subscriber -> subscriber.onNext(AuthUtils.getToken()));

		Subscription subscription = getTokenObservable.subscribe(token -> {
			for (SplashView splashView : getAttachedViews()) {
				splashView.setAuthorized(!TextUtils.isEmpty(token));
			}
		});
		unsubscribeOnDestroy(subscription);
	}
}
