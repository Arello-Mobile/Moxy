package com.arellomobile.mvp.presenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 17.12.2015
 * Time: 14:54
 *
 * @author Yuri Shmakov
 * @author Alexander BLinov
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectPresenter {
	String EMPTY = "";

	String tag() default EMPTY;

	PresenterType type() default PresenterType.LOCAL;

	String presenterId() default EMPTY;
}
