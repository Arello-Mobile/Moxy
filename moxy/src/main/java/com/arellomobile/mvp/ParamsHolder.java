package com.arellomobile.mvp;

import com.arellomobile.mvp.presenter.PresenterField;

/**
 * Date: 11-Jan-16
 * Time: 16:18
 *
 * @author Alexander Blinov
 */
public interface ParamsHolder<ReturnType> {
	/**
	 * Generate params for creating presenter instance
	 *
	 * @param presenterField generated class describes field
	 * @param delegated      view contains presenter
	 * @param delegateTag    key for current {@link MvpDelegate}
	 * @return params to creating presenter via factory
	 */
	ReturnType getParams(PresenterField<?> presenterField, Object delegated, String delegateTag);
}
