package com.arellomobile.mvp.compiler.viewstateprovider;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.TypeElement;

/**
 * Date: 27-Jul-2017
 * Time: 11:55
 *
 * @author Evgeny Kursakov
 */
class PresenterInfo {
	private final ClassName name;
	private final ClassName viewStateName;

	PresenterInfo(TypeElement name, String viewStateName) {
		this.name = ClassName.get(name);
		this.viewStateName = ClassName.bestGuess(viewStateName);
	}

	ClassName getName() {
		return name;
	}

	ClassName getViewStateName() {
		return viewStateName;
	}
}
