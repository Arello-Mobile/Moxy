package com.arellomobile.mvp;

/**
 * Date: 17-Dec-15
 * Time: 19:00
 *
 * @author Alexander Blinov
 */
public final class MvpFacade {
	private static volatile MvpFacade sInstance;

	private static final Object sLock = new Object();

	public static MvpFacade getInstance() {
		if (sInstance == null) {
			synchronized (sLock) {
				if (sInstance == null) {
					sInstance = new MvpFacade();
				}
			}
		}
		return sInstance;
	}

	public static void init() {
		getInstance();
	}

	private MvpFacade() {
		mPresenterStore = new PresenterStore();
		mMvpProcessor = new MvpProcessor();
		mPresenterFactoryStore = new PresenterFactoryStore();
	}

	private PresenterStore mPresenterStore;

	private MvpProcessor mMvpProcessor;

	private PresenterFactoryStore mPresenterFactoryStore;

	public PresenterStore getPresenterStore() {
		return mPresenterStore;
	}

	public PresenterFactoryStore getPresenterFactoryStore() {
		return mPresenterFactoryStore;
	}

	public MvpProcessor getMvpProcessor() {
		return mMvpProcessor;
	}
}
