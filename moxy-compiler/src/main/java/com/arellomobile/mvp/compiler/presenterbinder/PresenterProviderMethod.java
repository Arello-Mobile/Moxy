package com.arellomobile.mvp.compiler.presenterbinder;

import javax.lang.model.type.DeclaredType;

class PresenterProviderMethod {
	private final DeclaredType clazz;
	private final String name;
	private final String tag;
	private final String presenterId;

	PresenterProviderMethod(DeclaredType clazz, String name, String tag, String presenterId) {
		this.clazz = clazz;
		this.name = name;
		this.tag = tag;
		this.presenterId = presenterId;
	}

	DeclaredType getClazz() {
		return clazz;
	}

	String getName() {
		return name;
	}

	String getTag() {
		return tag;
	}

	String getPresenterId() {
		return presenterId;
	}
}
