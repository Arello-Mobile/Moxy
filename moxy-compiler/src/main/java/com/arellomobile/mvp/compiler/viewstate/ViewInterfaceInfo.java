package com.arellomobile.mvp.compiler.viewstate;

import com.google.common.collect.Iterables;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;

import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.TypeElement;

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

	ViewInterfaceInfo(TypeElement name, List<ViewMethod> methods) {
		this.name = ClassName.get(name);
		this.methods = methods;

		this.typeVariables = name.getTypeParameters().stream()
				.map(TypeVariableName::get)
				.collect(Collectors.toList());
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
