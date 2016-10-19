package com.arellomobile.mvp.presenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 14.10.2016
 * Time: 00:09
 *
 * @author Yuri Shmakov
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvidePresenter {
	PresenterType type() default PresenterType.LOCAL;

	String tag();
}
