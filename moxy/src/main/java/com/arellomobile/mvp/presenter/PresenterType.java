package com.arellomobile.mvp.presenter;

import com.arellomobile.mvp.MvpPresenter;

/**
 * Available presenter types. Manually lifetime control are available over {@link com.arellomobile.mvp.PresenterStore}, {@link com.arellomobile.mvp.PresentersCounter} and {@link MvpPresenter#onDestroy()}
 * <p>
 * Date: 17-Dec-15
 * Time: 19:31
 *
 * @author Yuri Shmakov
 * @author Alexander Blinov
 */
public enum PresenterType {
	/**
	 * Local presenter are not available out of injectable object
	 */
	LOCAL,
	/**
	 * Weak presenters are available everywhere. Weak presenter will be destroyed when finished all views. Inject will create new presenter instance.
	 */
	WEAK,
	/**
	 * Global presenter will be destroyed only when process will be killed({@link MvpPresenter#onDestroy()} won't be called)
	 */
	GLOBAL
}
