package com.arellomobile.mvp.compiler.presenterbinder;

import com.arellomobile.mvp.presenter.PresenterType;

import javax.lang.model.type.DeclaredType;

class PresenterProviderMethod {
	private final DeclaredType clazz;
	private final String name;
	private final PresenterType presenterType;
	private final String tag;
	private final String presenterId;

	PresenterProviderMethod(DeclaredType clazz, String name, String type, String tag, String presenterId) {
		this.clazz = clazz;
		this.name = name;
		if (type == null) {
			presenterType = PresenterType.LOCAL;
		} else {
			presenterType = PresenterType.valueOf(type);
		}
		this.tag = tag;
		this.presenterId = presenterId;
	}

	DeclaredType getClazz() {
		return clazz;
	}

	String getName() {
		return name;
	}

	PresenterType getPresenterType() {
		return presenterType;
	}

	String getTag() {
		return tag;
	}

	String getPresenterId() {
		return presenterId;
	}
}
