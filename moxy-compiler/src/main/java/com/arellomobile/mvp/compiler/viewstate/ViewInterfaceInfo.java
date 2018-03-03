package com.arellomobile.mvp.compiler.viewstate;

import com.google.common.collect.Iterables;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;

import java.util.List;

/**
 * Date: 27-Jul-2017
 * Time: 13:04
 *
 * @author Evgeny Kursakov
 */
class ViewInterfaceInfo {
	private final ClassName name;
	private final List<TypeVariableName> typeVariables;
	private final List<ViewMethod> methods;

	ViewInterfaceInfo(ClassName name, List<TypeVariableName> typeVariables, List<ViewMethod> methods) {
		this.name = name;
		this.typeVariables = typeVariables;
		this.methods = methods;
	}

	ClassName getName() {
		return name;
	}

	TypeName getNameWithTypeVariables() {
		if (typeVariables.isEmpty()) {
			return name;
		} else {
			return ParameterizedTypeName.get(name, Iterables.toArray(typeVariables, TypeVariableName.class));
		}
	}

	List<TypeVariableName> getTypeVariables() {
		return typeVariables;
	}

	List<ViewMethod> getMethods() {
		return methods;
	}
}
