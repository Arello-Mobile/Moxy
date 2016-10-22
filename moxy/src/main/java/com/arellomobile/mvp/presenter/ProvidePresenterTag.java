package com.arellomobile.mvp.presenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.arellomobile.mvp.MvpPresenter;

/**
 * <p>Called when Moxy generate presenter tag for search Presenter in {@link com.arellomobile.mvp.PresenterStore}.</p>
 * <p>Requirements:</p>
 * <ul>
 * <li>presenterClass parameter should be equals with presenter field type</li>
 * <li>Presenter Types should be same</li>
 * <li>Presenter IDs should be equals</li>
 * </ul>
 * <p>Note: if this method stay unused after build, then Moxy never use this method and you should check annotation parameters.</p>
 * <br>
 * Date: 14.10.2016
 * Time: 00:09
 *
 * @author Yuri Shmakov
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvidePresenterTag {
	String EMPTY = "";

	Class<? extends MvpPresenter<?>> presenterClass();

	PresenterType type() default PresenterType.LOCAL;

	String presenterId() default EMPTY;
}
