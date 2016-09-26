package com.arellomobile.mvp;

import android.app.Application;

/**
 * Date: 17-Dec-15
 * Time: 16:46
 *
 * @author Alexander Blinov
 */
public class MvpApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		MvpFacade.init();
	}
}
