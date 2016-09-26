package com.arellomobile.mvp;

import java.util.List;

import com.arellomobile.mvp.presenter.PresenterField;

/**
 * Date: 18-Dec-15
 * Time: 18:42
 *
 * @author Alexander Blinov
 */
public abstract class PresenterBinder<PresentersContainer> {
	protected PresentersContainer mTarget;

	public void setTarget(PresentersContainer presenterAggregator) {
		mTarget = presenterAggregator;
	}

	public abstract List<PresenterField<? super PresentersContainer>> getPresenterFields();
}
