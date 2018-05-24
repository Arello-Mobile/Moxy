package com.arellomobile.mvp.compiler.viewstate;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;

import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Date: 27-Jul-2017
 * Time: 12:58
 *
 * @author Evgeny Kursakov
 */
class ViewMethod {
	private final ExecutableElement element;
	private final String name;
	private final TypeElement strategy;
	private final String tag;
	private final List<ParameterSpec> parameterSpecs;
	private final List<TypeName> exceptions;
	private final List<TypeVariableName> typeVariables;
	private final String argumentsString;

	private String uniqueSuffix;

	ViewMethod(ExecutableElement methodElement,
	           TypeElement strategy,
	           String tag) {
		this.element = methodElement;
		this.name = methodElement.getSimpleName().toString();
		this.strategy = strategy;
		this.tag = tag;

		this.parameterSpecs = methodElement.getParameters()
				.stream()
				.map(ParameterSpec::get)
				.collect(Collectors.toList());

		this.exceptions = methodElement.getThrownTypes().stream()
				.map(TypeName::get)
				.collect(Collectors.toList());

		this.typeVariables = methodElement.getTypeParameters()
				.stream()
				.map(TypeVariableName::get)
				.collect(Collectors.toList());

		this.argumentsString = parameterSpecs.stream()
				.map(parameterSpec -> parameterSpec.name)
				.collect(Collectors.joining(", "));

		this.uniqueSuffix = "";
	}

	ExecutableElement getElement() {
		return element;
	}

	String getName() {
		return name;
	}

	TypeElement getStrategy() {
		return strategy;
	}

	String getTag() {
		return tag;
	}

	List<ParameterSpec> getParameterSpecs() {
		return parameterSpecs;
	}

	List<TypeName> getExceptions() {
		return exceptions;
	}

	List<TypeVariableName> getTypeVariables() {
		return typeVariables;
	}

	String getArgumentsString() {
		return argumentsString;
	}

	String getCommandClassName() {
		return name.substring(0, 1).toUpperCase() + name.substring(1) + uniqueSuffix + "Command";
	}

	String getEnclosedClassName() {
		TypeElement typeElement = (TypeElement) element.getEnclosingElement();
		return typeElement.getQualifiedName().toString();
	}

	String getUniqueSuffix() {
		return uniqueSuffix;
	}

	void setUniqueSuffix(String uniqueSuffix) {
		this.uniqueSuffix = uniqueSuffix;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ViewMethod that = (ViewMethod) o;

		return name.equals(that.name) && parameterSpecs.equals(that.parameterSpecs);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + parameterSpecs.hashCode();
		return result;
	}
}
