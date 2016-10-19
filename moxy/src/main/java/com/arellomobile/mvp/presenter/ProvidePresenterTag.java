package com.arellomobile.mvp.presenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Called when Moxy generate presenter tag for search Presenter in {@link com.arellomobile.mvp.PresenterStore}.</p>
 * <p>Requirements:</p>
 * <ul>
 * <li>Method should return full equals class as presenter field type</li>
 * <li>Presenter IDs should be equals</li>
 * </ul>
 * <p>Note: if this method stay unused after build, then Moxy never use this method and you should check annotation parameters.</p>
 * <br />
 * Date: 14.10.2016
 * Time: 00:09
 *
 * @author Yuri Shmakov
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvidePresenterTag {
	String EMPTY = "";

	String presenterId() default EMPTY;
}
