package com.omegar.mvp.compiler.viewstate;

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
	private final TypeElement element;
	private final ClassName name;
	private final List<TypeVariableName> typeVariables;
	private final List<ViewMethod> methods;

	ViewInterfaceInfo(TypeElement element, List<ViewMethod> methods) {
		this.element = element;
		this.name = ClassName.get(element);
		this.methods = methods;

		this.typeVariables = element.getTypeParameters().stream()
				.map(TypeVariableName::get)
				.collect(Collectors.toList());
	}

	public TypeElement getElement() {
		return element;
	}

	ClassName getName() {
		return name;
	}

	TypeName getNameWithTypeVariables() {
		if (typeVariables.isEmpty()) {
			return name;
		} else {
			TypeVariableName[] names = new TypeVariableName[typeVariables.size()];
			typeVariables.toArray(names);

			return ParameterizedTypeName.get(name, names);
		}
	}

	List<TypeVariableName> getTypeVariables() {
		return typeVariables;
	}

	List<ViewMethod> getMethods() {
		return methods;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ViewInterfaceInfo that = (ViewInterfaceInfo) o;

		return name != null ? name.equals(that.name) : that.name == null;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
