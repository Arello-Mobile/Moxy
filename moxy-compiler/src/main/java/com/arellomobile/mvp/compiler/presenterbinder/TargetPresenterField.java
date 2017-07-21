package com.arellomobile.mvp.compiler.presenterbinder;

import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.presenter.PresenterType;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

class TargetPresenterField {
	private final TypeMirror clazz;
	private final TypeName typeName;
	private final String name;
	private final PresenterType type;
	private final String tag;
	private final String presenterId;

	private String presenterProviderMethodName;
	private String presenterTagProviderMethodName;

	TargetPresenterField(TypeMirror clazz, String name, String type, String tag, String presenterId) {
		this.clazz = clazz;
		this.typeName = TypeName.get(clazz);
		this.name = name;
		this.tag = tag;


		if (type == null) {
			this.type = PresenterType.LOCAL;
		} else {
			this.type = PresenterType.valueOf(type);
		}

		this.presenterId = presenterId;
	}

	TypeMirror getClazz() {
		return clazz;
	}

	TypeName getTypeName() {
		return typeName;
	}

	String getGeneratedClassName() {
		return name + MvpProcessor.PRESENTER_BINDER_INNER_SUFFIX;
	}

	String getTag() {
		return tag;
	}

	String getName() {
		return name;
	}

	PresenterType getType() {
		return type;
	}

	String getPresenterId() {
		return presenterId;
	}

	String getPresenterProviderMethodName() {
		return presenterProviderMethodName;
	}

	void setPresenterProviderMethodName(String presenterProviderMethodName) {
		this.presenterProviderMethodName = presenterProviderMethodName;
	}

	String getPresenterTagProviderMethodName() {
		return presenterTagProviderMethodName;
	}

	void setPresenterTagProviderMethodName(String presenterTagProviderMethodName) {
		this.presenterTagProviderMethodName = presenterTagProviderMethodName;
	}
}
