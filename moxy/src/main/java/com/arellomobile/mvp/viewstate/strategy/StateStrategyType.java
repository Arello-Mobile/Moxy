package com.arellomobile.mvp.viewstate.strategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 16-Dec-15
 * Time: 17:07
 *
 * @author Yuri Shmakov
 * @author Alexander Blinov
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface StateStrategyType {
	Class<? extends StateStrategy> value();

	String tag() default "";
}
