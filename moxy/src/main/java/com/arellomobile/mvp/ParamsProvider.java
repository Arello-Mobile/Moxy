package com.arellomobile.mvp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 12-jan-15
 * Time: 10:48
 *
 * @author Alexander Blinov
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ParamsProvider {
	Class<? extends PresenterFactory<? extends MvpPresenter<?>, ?>>[] value();
}
