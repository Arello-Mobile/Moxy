package com.arellomobile.mvp.compiler.presenterbinder;

import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.element.TypeElement;

class TargetClassInfo {
	private final TypeElement name;
	private final List<TargetPresenterField> fields;

	TargetClassInfo(TypeElement name, List<TargetPresenterField> fields) {
		this.name = name;
		this.fields = fields;
	}

	ClassName getName() {
		return ClassName.get(name);
	}

	List<TargetPresenterField> getFields() {
		return fields;
	}

	TypeElement getElement() {
		return name;
	}
}
