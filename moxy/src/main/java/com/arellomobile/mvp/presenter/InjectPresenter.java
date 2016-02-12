package com.arellomobile.mvp.presenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.arellomobile.mvp.DefaultPresenterFactory;
import com.arellomobile.mvp.PresenterFactory;

/**
 * Date: 17.12.2015
 * Time: 14:54
 *
 * @author Yuri Shmakov
 * @author Alexander BLinov
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectPresenter
{
	String EMPTY = "";

	PresenterType type() default PresenterType.LOCAL;

	String tag() default "";

	Class<? extends PresenterFactory> factory() default DefaultPresenterFactory.class;

	String presenterId() default EMPTY;
}
