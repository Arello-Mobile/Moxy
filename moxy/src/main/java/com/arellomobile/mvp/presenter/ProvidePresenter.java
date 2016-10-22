package com.arellomobile.mvp.presenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Called when Moxy can't find right presenter instance in {@link com.arellomobile.mvp.PresenterStore}.
 * <p>Attention! <b>Don't use manually method marked with this annotation!</b> Use presenter field, which you want. If you override this method in inherited classes, make them return same type(not requirements but recommendation).</p>
 * <p>Requirements:</p>
 * <ul>
 * <li>Method should return full equals class as presenter field type</li>
 * <li>Presenter Types should be same</li>
 * <li>Tags should be equals</li>
 * <li>Presenter IDs should be equals</li>
 * </ul>
 * <p>Note: if this method stay unused after build, then Moxy never use this method and you should check annotation parameters. These parameters should be equals to @InjectPresenter parameters</p>
 * <br>
 * Date: 14.10.2016
 * Time: 00:09
 *
 * @author Yuri Shmakov
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvidePresenter {
	String EMPTY = "";

	String tag() default EMPTY;

	PresenterType type() default PresenterType.LOCAL;

	String presenterId() default EMPTY;
}
