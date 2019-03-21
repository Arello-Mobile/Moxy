package com.omegar.mvp.presenter;

import com.omegar.mvp.MvpPresenter;
import com.omegar.mvp.PresenterStore;
import com.omegar.mvp.PresentersCounter;

/**
 * Available presenter types. Manually lifetime control are available over {@link PresenterStore}, {@link PresentersCounter} and {@link MvpPresenter#onDestroy()}
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
