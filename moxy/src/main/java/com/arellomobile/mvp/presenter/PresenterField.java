package com.arellomobile.mvp.presenter;

import com.arellomobile.mvp.*;

/**
 * Date: 18-Dec-15
 * Time: 17:50
 *
 * @author Alexander Blinov
 * @author Yuri Shmakov
 */
public abstract class PresenterField<PresentersContainer> {
	protected final String tag;
	protected final PresenterType presenterType;
	protected final String presenterId;
	protected final Class<? extends IMvpPresenter> presenterClass;

	protected PresenterField(String tag, PresenterType presenterType, String presenterId, Class<? extends IMvpPresenter> presenterClass) {
		this.tag = tag;
		this.presenterType = presenterType;
		this.presenterId = presenterId;
		this.presenterClass = presenterClass;
	}

	public abstract void bind(PresentersContainer container, MvpPresenter presenter);

	// Delegated may be used from generated code if user plane to generate tag at runtime
	@SuppressWarnings("UnusedParameters")
	public String getTag(PresentersContainer delegated) {
		return tag;
	}

	public PresenterType getPresenterType() {
		return presenterType;
	}

	public String getPresenterId() {
		return presenterId;
	}

	public Class<? extends IMvpPresenter> getPresenterClass() {
		return presenterClass;
	}

	public abstract MvpPresenter<?> providePresenter(PresentersContainer delegated);
}
