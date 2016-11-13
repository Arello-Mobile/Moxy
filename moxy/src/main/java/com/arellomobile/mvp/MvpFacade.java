package com.arellomobile.mvp;

/**
 * Date: 17-Dec-15
 * Time: 19:00
 *
 * @author Alexander Blinov
 * @author Yuri Shmakov
 */
@SuppressWarnings({"WeakerAccess", "unused"})
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
		mPresentersCounter = new PresentersCounter();
		mPresenterStore = new PresenterStore();
		mMvpProcessor = new MvpProcessor();
	}

	private PresenterStore mPresenterStore;
	private MvpProcessor mMvpProcessor;
	private PresentersCounter mPresentersCounter;

	public PresenterStore getPresenterStore() {
		return mPresenterStore;
	}

	public void setPresenterStore(PresenterStore presenterStore) {
		mPresenterStore = presenterStore;
	}

	public MvpProcessor getMvpProcessor() {
		return mMvpProcessor;
	}

	public void setMvpProcessor(MvpProcessor mvpProcessor) {
		mMvpProcessor = mvpProcessor;
	}

	public PresentersCounter getPresentersCounter() {
		return mPresentersCounter;
	}

	public void setPresentersCounter(PresentersCounter presentersCounter) {
		mPresentersCounter = presentersCounter;
	}
}
