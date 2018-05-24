package com.arellomobile.mvp.compiler.presenterbinder;

import com.arellomobile.mvp.presenter.PresenterType;

import javax.lang.model.type.TypeMirror;

class TagProviderMethod {
	private final TypeMirror presenterClass;
	private final String methodName;
	private final PresenterType type;
	private final String presenterId;

	TagProviderMethod(TypeMirror presenterClass, String methodName, String type, String presenterId) {
		this.presenterClass = presenterClass;
		this.methodName = methodName;
		if (type == null) {
			this.type = PresenterType.LOCAL;
		} else {
			this.type = PresenterType.valueOf(type);
		}
		this.presenterId = presenterId;
	}

	TypeMirror getPresenterClass() {
		return presenterClass;
	}

	String getMethodName() {
		return methodName;
	}

	PresenterType getType() {
		return type;
	}

	String getPresenterId() {
		return presenterId;
	}
}
