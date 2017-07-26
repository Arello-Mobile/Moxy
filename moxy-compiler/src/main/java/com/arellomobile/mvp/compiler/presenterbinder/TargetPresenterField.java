package com.arellomobile.mvp.compiler.presenterbinder;

import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.presenter.PresenterType;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

class TargetPresenterField {
	private final TypeMirror clazz;
	private final TypeName typeName;
	private final String name;
	private final PresenterType presenterType;
	private final String tag;
	private final String presenterId;

	private String presenterProviderMethodName;
	private String presenterTagProviderMethodName;

	TargetPresenterField(TypeMirror clazz,
	                     String name,
	                     String presenterType,
	                     String tag,
	                     String presenterId) {
		this.clazz = clazz;
		this.typeName = TypeName.get(clazz);
		this.name = name;
		this.tag = tag;

		if (presenterType == null) {
			this.presenterType = PresenterType.LOCAL;
		} else {
			this.presenterType = PresenterType.valueOf(presenterType);
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

	PresenterType getPresenterType() {
		return presenterType;
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
