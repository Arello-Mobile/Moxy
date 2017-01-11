package com.arellomobile.mvp.sample.github.mvp.presenters;

import android.text.TextUtils;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.sample.github.mvp.common.AuthUtils;
import com.arellomobile.mvp.sample.github.mvp.views.SplashView;

/**
 * This presenter's View doesn't want ViewState.
 *
 * Date: 18.01.2016
 * Time: 15:38
 *
 * @author Yuri Shmakov
 */
public class SplashPresenter extends MvpPresenter<SplashView> {

	@Override
	public void attachView(SplashView view) {
		super.attachView(view);

		view.setAuthorized(!TextUtils.isEmpty(AuthUtils.getToken()));
	}
}
