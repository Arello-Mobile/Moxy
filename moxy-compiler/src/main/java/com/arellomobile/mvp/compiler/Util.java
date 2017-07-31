/*
 * Copyright (C) 2013 Google, Inc.
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arellomobile.mvp.compiler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

/**
 * Utilities for handling types in annotation processors
 *
 * @author Yuri Shmakov
 */
@SuppressWarnings("WeakerAccess")
public final class Util {
	public static String fillGenerics(Map<String, String> types, TypeMirror param) {
		return fillGenerics(types, Collections.singletonList(param));
	}

	public static String fillGenerics(Map<String, String> types, List<? extends TypeMirror> params) {
		return fillGenerics(types, params, ", ");
	}

	public static String fillGenerics(Map<String, String> types, List<? extends TypeMirror> params, String separator) {
		String result = "";

		for (TypeMirror param : params) {
			if (result.length() > 0) {
				result += separator;
			}

			/**
			 * "if" block's order is critically! E.g. IntersectionType is TypeVariable.
			 */
			if (param instanceof WildcardType) {
				result += "?";
				final TypeMirror extendsBound = ((WildcardType) param).getExtendsBound();
				if (extendsBound != null) {
					result += " extends " + fillGenerics(types, extendsBound);
				}
				final TypeMirror superBound = ((WildcardType) param).getSuperBound();
				if (superBound != null) {
					result += " super " + fillGenerics(types, superBound);
				}
			} else if (param instanceof IntersectionType) {
				result += "?";
				final List<? extends TypeMirror> bounds = ((IntersectionType) param).getBounds();

				if (!bounds.isEmpty()) {
					result += " extends " + fillGenerics(types, bounds, " & ");
				}
			} else if (param instanceof DeclaredType) {
				result += ((DeclaredType) param).asElement();

				final List<? extends TypeMirror> typeArguments = ((DeclaredType) param).getTypeArguments();
				if (!typeArguments.isEmpty()) {
					final String s = fillGenerics(types, typeArguments);

					result += "<" + s + ">";
				}
			} else if (param instanceof TypeVariable) {
				String type = types.get(param.toString());
				if (type == null) {
					type = param.toString();
				}
				result += type;
			} else {
				result += param;
			}
		}

		return result;
	}

	public static String getFullClassName(TypeMirror typeMirror) {
		if (!(typeMirror instanceof DeclaredType)) {
			return "";
		}

		TypeElement typeElement = (TypeElement) ((DeclaredType) typeMirror).asElement();
		return getFullClassName(typeElement);
	}

	public static String getFullClassName(TypeElement typeElement) {
		String packageName = MvpCompiler.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
		if (packageName.length() > 0) {
			packageName += ".";
		}

		String className = typeElement.toString().substring(packageName.length());
		return packageName + className.replaceAll("\\.", "\\$");
	}

	public static AnnotationMirror getAnnotation(Element element, String annotationClass) {
		for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
			if (annotationMirror.getAnnotationType().asElement().toString().equals(annotationClass))
				return annotationMirror;
		}

		return null;
	}

	public static TypeMirror getAnnotationValueAsTypeMirror(AnnotationMirror annotationMirror, String key) {
		AnnotationValue av = getAnnotationValue(annotationMirror, key);

		if (av != null) {
			return (TypeMirror) av.getValue();
		} else {
			return null;
		}
	}

	public static String getAnnotationValueAsString(AnnotationMirror annotationMirror, String key) {
		AnnotationValue av = getAnnotationValue(annotationMirror, key);

		if (av != null) {
			return av.getValue().toString();
		} else {
			return null;
		}
	}

	public static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String key) {
		if (annotationMirror == null) return null;

		for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
			if (entry.getKey().getSimpleName().toString().equals(key)) {
				return entry.getValue();
			}
		}

		return null;
	}

	public static Map<String, AnnotationValue> getAnnotationValues(AnnotationMirror annotationMirror) {
		if (annotationMirror == null) return Collections.emptyMap();

		Map<String, AnnotationValue> result = new HashMap<>();

		for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
			String key = entry.getKey().getSimpleName().toString();
			if (entry.getValue() != null) {
				result.put(key, entry.getValue());
			}
		}

		return result;
	}

	public static boolean hasEmptyConstructor(TypeElement element) {
		for (Element enclosedElement : element.getEnclosedElements()) {
			if (enclosedElement.getKind() == ElementKind.CONSTRUCTOR) {
				List<? extends VariableElement> parameters = ((ExecutableElement) enclosedElement).getParameters();
				if (parameters == null || parameters.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	public static String decapitalizeString(String string) {
		return string == null || string.isEmpty() ? "" : string.length() == 1 ? string.toLowerCase() : Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}
}
