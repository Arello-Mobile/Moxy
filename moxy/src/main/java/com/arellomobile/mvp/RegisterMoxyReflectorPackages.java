package com.arellomobile.mvp;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Register MoxyReflector packages from other modules
 */
@Target(value = TYPE)
public @interface RegisterMoxyReflectorPackages {
	String[] value();
}
