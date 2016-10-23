package com.arellomobile.mvp.sample.github.mvp.presenters;

import com.arellomobile.mvp.sample.github.mvp.common.AuthUtils;
import com.arellomobile.mvp.sample.github.mvp.views.SignOutView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

/**
 * Date: 18.01.2016
 * Time: 16:03
 *
 * @author Yuri Shmakov
 */
@InjectViewState
public class SignOutPresenter extends MvpPresenter<SignOutView> {
	public void signOut() {
		AuthUtils.setToken("");

		getViewState().signOut();
	}
}
