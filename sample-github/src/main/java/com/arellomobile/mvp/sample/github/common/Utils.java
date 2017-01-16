package com.arellomobile.mvp.sample.github.common;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Date: 11.01.2017
 * Time: 16:39
 *
 * @author Yuri Shmakov
 */

public class Utils {
	public static <T> Observable.Transformer<T, T> applySchedulers() {
		return observable -> observable.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
