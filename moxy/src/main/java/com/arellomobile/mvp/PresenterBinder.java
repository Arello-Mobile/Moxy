package com.arellomobile.mvp;

import java.util.List;

import com.arellomobile.mvp.presenter.PresenterField;

/**
 * Date: 18-Dec-15
 * Time: 18:42
 *
 * @author Alexander Blinov
 */
public interface PresenterBinder<PresentersContainer>
{
	List<PresenterField<? super PresentersContainer>> getPresenterFields();

	void setTarget(PresentersContainer presenterAggregator);
}
