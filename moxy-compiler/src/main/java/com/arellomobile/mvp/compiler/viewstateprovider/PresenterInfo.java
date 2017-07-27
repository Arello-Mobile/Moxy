package com.arellomobile.mvp.compiler.viewstateprovider;

import com.squareup.javapoet.ClassName;

/**
 * Date: 27-Jul-2017
 * Time: 11:55
 *
 * @author Evgeny Kursakov
 */
class PresenterInfo {
	private final ClassName name;
	private final ClassName viewStateName;

	PresenterInfo(ClassName name, ClassName viewStateName) {
		this.name = name;
		this.viewStateName = viewStateName;
	}

	ClassName getName() {
		return name;
	}

	ClassName getViewStateName() {
		return viewStateName;
	}
}
