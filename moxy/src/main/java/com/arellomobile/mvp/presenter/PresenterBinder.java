package com.arellomobile.mvp.presenter;

/**
 * Date: 18-Dec-15
 * Time: 12:48
 *
 * @author Alexander Blinov
 */
public interface PresenterBinder<T>
{
	void bind(T target, Object source);

	void unbind(T target);
}
