package com.arellomobile.mvp.compiler.viewstate;

import com.arellomobile.mvp.compiler.MvpCompiler;
import com.arellomobile.mvp.compiler.Util;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Date: 27-Jul-2017
 * Time: 12:58
 *
 * @author Evgeny Kursakov
 */
class ViewMethod {
	private final ExecutableElement methodElement;
	private final String name;
	private final TypeElement strategy;
	private final String tag;
	private final List<ParameterSpec> parameterSpecs;
	private final List<TypeName> exceptions;
	private final List<TypeVariableName> typeVariables;
	private final String argumentsString;

	private String uniqueSuffix;

	ViewMethod(DeclaredType targetInterfaceElement,
	           ExecutableElement methodElement,
	           TypeElement strategy,
	           String tag) {
		this.methodElement = methodElement;
		this.name = methodElement.getSimpleName().toString();
		this.strategy = strategy;
		this.tag = tag;

		this.parameterSpecs = new ArrayList<>();

		Types typeUtils = MvpCompiler.getTypeUtils();
		ExecutableType executableType = (ExecutableType) typeUtils.asMemberOf(targetInterfaceElement, methodElement);
		List<? extends VariableElement> parameters = methodElement.getParameters();
		List<? extends TypeMirror> resolvedParameterTypes = executableType.getParameterTypes();

		for (int i = 0; i < parameters.size(); i++) {
			VariableElement element = parameters.get(i);
			TypeName type = TypeName.get(resolvedParameterTypes.get(i));
			String name = element.getSimpleName().toString();

			parameterSpecs.add(ParameterSpec.builder(type, name)
					.addModifiers(element.getModifiers())
					.build()
			);
		}

		this.exceptions = methodElement.getThrownTypes().stream()
				.map(TypeName::get)
				.collect(Collectors.toList());

		this.typeVariables = methodElement.getTypeParameters()
				.stream()
				.map(TypeVariableName::get)
				.collect(Collectors.toList());

		this.argumentsString = this.parameterSpecs.stream()
				.map(parameterSpec -> parameterSpec.name)
				.collect(Collectors.joining(", "));

		this.uniqueSuffix = "";
	}

	ExecutableElement getElement() {
		return methodElement;
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
		TypeElement typeElement = (TypeElement) methodElement.getEnclosingElement();
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

		return Objects.equals(name, that.name) && Util.equalsBy(parameterSpecs, that.parameterSpecs,
				(first, second) -> Objects.equals(first.type, second.type));
	}

	@Override
	public int hashCode() {
		if (name == null && parameterSpecs == null) {
			return 0;
		}
		int result = name != null ? name.hashCode() : 1;
		result = 31 * result;
		for (ParameterSpec spec : parameterSpecs) {
			result = 31 * result + (spec != null ? Objects.hashCode(spec.type) : 0);
		}
		return result;
	}
}
