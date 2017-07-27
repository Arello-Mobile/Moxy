package com.arellomobile.mvp.compiler.presenterbinder;

import com.squareup.javapoet.ClassName;

import java.util.List;

class TargetClassInfo {
	private final ClassName name;
	private final List<TargetPresenterField> fields;

	TargetClassInfo(ClassName name, List<TargetPresenterField> fields) {
		this.name = name;
		this.fields = fields;
	}

	ClassName getName() {
		return name;
	}

	List<TargetPresenterField> getFields() {
		return fields;
	}
}
