package com.arellomobile.mvp.compiler.viewstate;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

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

		this.exceptions = element.getThrownTypes().stream()
				.map(TypeName::get)
				.collect(Collectors.toList());

		this.argumentsString = parameterSpecs.stream()
				.map(parameterSpec -> parameterSpec.name)
				.collect(Collectors.joining(", "));

		this.uniqueSuffix = "";
	}

	public ExecutableElement getElement() {
		return element;
	}

	public String getName() {
		return name;
	}

	public TypeElement getStrategy() {
		return strategy;
	}

	public String getTag() {
		return tag;
	}

	public List<ParameterSpec> getParameterSpecs() {
		return parameterSpecs;
	}

	public List<TypeName> getExceptions() {
		return exceptions;
	}

	public String getArgumentsString() {
		return argumentsString;
	}

	String getCommandClassName() {
		return name.substring(0, 1).toUpperCase() + name.substring(1) + uniqueSuffix + "Command";
	}

	String getEnclosedClassName() {
		TypeElement typeElement = (TypeElement) element.getEnclosingElement();
		return typeElement.getQualifiedName().toString();
	}

	public String getUniqueSuffix() {
		return uniqueSuffix;
	}

	public void setUniqueSuffix(String uniqueSuffix) {
		this.uniqueSuffix = uniqueSuffix;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ViewMethod that = (ViewMethod) o;

		if (element != null ? !element.equals(that.element) : that.element != null) return false;
		return uniqueSuffix != null ? uniqueSuffix.equals(that.uniqueSuffix) : that.uniqueSuffix == null;

	}

	@Override
	public int hashCode() {
		int result = element != null ? element.hashCode() : 0;
		result = 31 * result + (uniqueSuffix != null ? uniqueSuffix.hashCode() : 0);
		return result;
	}
}
