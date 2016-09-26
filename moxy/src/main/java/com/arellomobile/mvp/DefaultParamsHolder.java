package com.arellomobile.mvp;

import com.arellomobile.mvp.presenter.PresenterField;

/**
 * Date: 11-Jan-16
 * Time: 14:04
 *
 * @author Alexander Blinov
 */
public class DefaultParamsHolder implements ParamsHolder<DefaultPresenterFactory.Params> {
	@Override
	public DefaultPresenterFactory.Params getParams(PresenterField<?> presenterField, Object delegated, String delegateTag) {
		return new DefaultPresenterFactory.Params(delegateTag, presenterField.getClass().getSimpleName(), presenterField.getTag());
	}
}
