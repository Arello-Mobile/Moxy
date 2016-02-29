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
import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.tools.Diagnostic;

/**
 * Utilities for handling types in annotation processors
 */
final class Util
{
	public static String fillGenerics(Map<String, String> types, TypeMirror param)
	{
		return fillGenerics(types, Collections.singletonList(param));
	}

	public static String fillGenerics(Map<String, String> types, List<? extends TypeMirror> params)
	{
		return fillGenerics(types, params, ", ");
	}

	public static String fillGenerics(Map<String, String> types, List<? extends TypeMirror> params, String separator)
	{
		String result = "";

		for (TypeMirror param : params)
		{
			MvpCompiler.getMessager().printMessage(Diagnostic.Kind.NOTE, "Param: " + param + ", type: " + param.getKind());

			if (result.length() > 0)
			{
				result += separator;
			}

			/**
			 * "if" block's order is critically! E.g. IntersectionType is TypeVariable.
			 */
			if (param instanceof WildcardType)
			{
				result += "?";
				final TypeMirror extendsBound = ((WildcardType) param).getExtendsBound();
				if (extendsBound != null)
				{
					result += " extends " + fillGenerics(types, extendsBound);
				}
				final TypeMirror superBound = ((WildcardType) param).getSuperBound();
				if (superBound != null)
				{
					result += " super " + fillGenerics(types, superBound);
				}
			}
			else if (param instanceof IntersectionType)
			{
				result += "?";
				final List<? extends TypeMirror> bounds = ((IntersectionType) param).getBounds();

				if (!bounds.isEmpty())
				{
					result += " extends " + fillGenerics(types, bounds, " & ");
				}
			}
			else if (param instanceof DeclaredType)
			{
				result += ((DeclaredType) param).asElement();

				final List<? extends TypeMirror> typeArguments = ((DeclaredType) param).getTypeArguments();
				if (!typeArguments.isEmpty())
				{
					final String s = fillGenerics(types, typeArguments);

					result += "<" + s + ">";
				}
			}
			else if (param instanceof TypeVariable)
			{
				result += types.get(param.toString());
			}
			else
			{
				result += param;
			}
		}

		return result;
	}

	public static String getFullClassName(TypeMirror typeMirror)
	{
		if (!(typeMirror instanceof DeclaredType))
		{
			return "";
		}

		TypeElement typeElement = (TypeElement) ((DeclaredType) typeMirror).asElement();
		return getFullClassName(typeElement);
	}

	public static String getFullClassName(TypeElement typeElement)
	{
		String packageName = MvpCompiler.getElementUtils().getPackageOf(typeElement).toString();
		if (packageName.length() > 0)
		{
			packageName += ".";
		}
		String className = typeElement.toString().substring(packageName.length());
		return packageName + className.replaceAll("\\.", "\\$");
	}
}
