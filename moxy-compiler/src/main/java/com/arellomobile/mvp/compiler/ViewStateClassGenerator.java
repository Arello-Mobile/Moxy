package com.arellomobile.mvp.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;


import static com.arellomobile.mvp.compiler.Util.fillGenerics;

/**
 * Date: 18.12.2015
 * Time: 13:24
 *
 * @author Yuri Shmakov
 */
final class ViewStateClassGenerator extends ClassGenerator<TypeElement>
{
	public static final String STATE_STRATEGY_TYPE_ANNOTATION = StateStrategyType.class.getName();
	public static final String DEFAULT_STATE_STRATEGY = AddToEndStrategy.class.getName() + ".class";

	private String mViewClassName;

	public boolean generate(TypeElement typeElement, List<ClassGeneratingParams> classGeneratingParamsList)
	{
		if (!typeElement.getTypeParameters().isEmpty())
		{
			throw new IllegalStateException("Code generation can't be applied to generic interface " + typeElement.getSimpleName());
		}

		ClassGeneratingParams classGeneratingParams = new ClassGeneratingParams();
		classGeneratingParams.setName(typeElement + MvpProcessor.VIEW_STATE_SUFFIX);

		mViewClassName = getClassName(typeElement);

		String builder = "package " + typeElement.toString().substring(0, typeElement.toString().lastIndexOf(".")) + ";\n" +
				"\n" +
				"import com.arellomobile.mvp.viewstate.MvpViewState;\n" +
				"import com.arellomobile.mvp.viewstate.ViewCommand;\n" +
				"import com.arellomobile.mvp.viewstate.ViewCommands;\n" +
				"import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;\n" +
				"import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;\n" +
				"import com.arellomobile.mvp.viewstate.strategy.StateStrategy;\n" +
				"\n" +
				"public class " + mViewClassName + "$$State extends MvpViewState<" + mViewClassName + "> implements " + mViewClassName + "\n" +
				"{\n" +
				"\tprivate ViewCommands<" + mViewClassName + "> mViewCommands = new ViewCommands<>();\n" +
				"\n" +
				"\t@Override\n" +
				"\tpublic void restoreState(" + mViewClassName + " view)\n" +
				"\t{\n" +
				"\t\tif (mViewCommands.isEmpty())\n" +
				"\t\t{\n" +
				"\t\t\treturn;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tmViewCommands.reapply(view);\n" +
				"\t}\n" +
				"\n";

		List<Method> methods = new ArrayList<>();

		// Get methods for input class
		getMethods(Collections.<String, String>emptyMap(), typeElement, new ArrayList<Method>(), methods);

		// Add methods from super intefaces
		methods.addAll(iterateInterfaces(0, typeElement, Collections.<String, String>emptyMap(), methods, new ArrayList<Method>()));

		// Allow methods be with same names
		Map<String, Integer> methodsCounter = new HashMap<>();
		for (Method method : methods)
		{
			Integer counter = methodsCounter.get(method.name);

			if (counter == null || counter == 0)
			{
				counter = 0;
				method.uniqueName = method.name;
			}
			else
			{
				method.uniqueName = method.name + counter;
			}

			method.paramsClassName = method.uniqueName.substring(0, 1).toUpperCase() + method.uniqueName.substring(1) + "Params";

			counter++;
			methodsCounter.put(method.name, counter);
		}

		for (Method method : methods)
		{
			String throwTypesString = join(", ", method.thrownTypes);
			if (throwTypesString.length() > 0)
			{
				throwTypesString = " throws " + throwTypesString;
			}

			String argumentsString = "";
			for (Argument argument : method.arguments)
			{
				if (argumentsString.length() > 0)
				{
					argumentsString += ", ";
				}
				argumentsString += argument.name;
			}

			String argumentClassName = "Void";
			String argumentsWrapperNewInstance = "null;\n";
			if (argumentsString.length() > 0)
			{
				argumentClassName = method.paramsClassName;
				argumentsWrapperNewInstance = "new " + method.paramsClassName + "(" + argumentsString + ");\n";
			}

			builder += "\t@Override\n" +
					"\tpublic " + method.genericType + method.resultType + " " + method.name + "(" + join(", ", method.arguments) + ")" + throwTypesString + "\n" +
					"\t{\n" +
					"\t\t" + argumentClassName + " params = " + argumentsWrapperNewInstance +
					"\t\tmViewCommands.beforeApply(LocalViewCommand." + method.uniqueName + ", params);\n" +
					"\n" +
					"\t\tif (mViews == null || mViews.isEmpty())\n" +
					"\t\t{\n" +
					"\t\t\treturn;\n" +
					"\t\t}\n" +
					"\n" +
					"\t\tfor(" + mViewClassName + " view : mViews)\n" +
					"\t\t{\n" +
					"\t\t\tview." + method.name + "(" + argumentsString + ");\n" +
					"\t\t}\n" +
					"\n" +
					"\t\tmViewCommands.afterApply(LocalViewCommand." + method.uniqueName + ", params);\n" +
					"\t}\n" +
					"\n";
		}

		if (!methods.isEmpty())
		{
			builder = generateLocalViewCommand(mViewClassName, builder, methods);
		}

		builder += "}\n";

		classGeneratingParams.setBody(builder);
		classGeneratingParamsList.add(classGeneratingParams);

		return true;
	}

	private List<Method> iterateInterfaces(int level, TypeElement parentElement, Map<String, String> parentTypes, List<Method> rootMethods, List<Method> superinterfacesMethods)
	{
		for (TypeMirror typeMirror : parentElement.getInterfaces())
		{
			final TypeElement anInterface = (TypeElement) ((DeclaredType) typeMirror).asElement();

			final List<? extends TypeMirror> typeArguments = ((DeclaredType) typeMirror).getTypeArguments();
			final List<? extends TypeParameterElement> typeParameters = anInterface.getTypeParameters();

			if (typeArguments.size() != typeParameters.size())
			{
				throw new IllegalArgumentException("Code generation for interface " + anInterface.getSimpleName() + " failed. Simplify your generics.");
			}

			Map<String, String> types = new HashMap<>();
			for (int i = 0; i < typeArguments.size(); i++)
			{
				types.put(typeParameters.get(i).toString(), typeArguments.get(i).toString());
			}

			Map<String, String> totalInterfaceTypes = new HashMap<>(typeParameters.size());
			for (int i = 0; i < typeArguments.size(); i++)
			{
				totalInterfaceTypes.put(typeParameters.get(i).toString(), fillGenerics(parentTypes, typeArguments.get(i)));

			}

			getMethods(totalInterfaceTypes, anInterface, rootMethods, superinterfacesMethods);

			iterateInterfaces(level + 1, anInterface, types, rootMethods, superinterfacesMethods);
		}

		return superinterfacesMethods;
	}

	private List<Method> getMethods(Map<String, String> types, TypeElement typeElement, List<Method> rootMethods, List<Method> superinterfacesMethods)
	{
		for (Element element : typeElement.getEnclosedElements())
		{
			if (!(element instanceof ExecutableElement))
			{
				continue;
			}

			final ExecutableElement methodElement = (ExecutableElement) element;

			String strategyClass = getStateStrategyType(typeElement);
			String methodTag = "\"" + methodElement.getSimpleName() + "\"";
			for (AnnotationMirror annotationMirror : methodElement.getAnnotationMirrors())
			{
				if (!annotationMirror.getAnnotationType().asElement().toString().equals(STATE_STRATEGY_TYPE_ANNOTATION))
				{
					continue;
				}

				final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
				final Set<? extends ExecutableElement> keySet = elementValues.keySet();

				for (ExecutableElement key : keySet)
				{
					if ("value()".equals(key.toString()))
					{
						strategyClass = elementValues.get(key).toString();
					}
					else if ("tag()".equals(key.toString()))
					{
						methodTag = elementValues.get(key).toString();
					}
				}
			}

			Map<String, String> methodTypes = new HashMap<>(types);

			final ExecutableType executableType = (ExecutableType) methodElement.asType();
			final List<? extends TypeVariable> typeVariables = executableType.getTypeVariables();
			if (!typeVariables.isEmpty())
			{
				for (TypeVariable typeVariable : typeVariables)
				{
					methodTypes.put(typeVariable.asElement().toString(), typeVariable.asElement().toString());
				}
			}

			String generics = "";
			int genericsCount = typeVariables.size();

			if (!typeVariables.isEmpty())
			{
				generics += "<";
				for (TypeVariable typeVariable : typeVariables)
				{
					if (generics.length() > 1)
					{
						generics += ", ";
					}

					generics += typeVariable.asElement();

					final TypeMirror upperBound = typeVariable.getUpperBound();

					if (upperBound.toString().equals(Object.class.getCanonicalName()))
					{
						continue;
					}

					final String filledGeneric = fillGenerics(methodTypes, upperBound);
					generics += filledGeneric.substring(1);
				}
				generics += "> ";
			}

			final List<? extends VariableElement> parameters = methodElement.getParameters();

			List<Argument> arguments = new ArrayList<>();
			for (VariableElement parameter : parameters)
			{
				arguments.add(new Argument(fillGenerics(methodTypes, parameter.asType()), parameter.toString()));
			}

			List<String> throwTypes = new ArrayList<>();
			for (TypeMirror typeMirror : methodElement.getThrownTypes())
			{
				throwTypes.add(fillGenerics(methodTypes, typeMirror));
			}

			final Method method = new Method(genericsCount, generics, fillGenerics(methodTypes, methodElement.getReturnType()), methodElement.getSimpleName().toString(), arguments, throwTypes, strategyClass, methodTag, getClassName(typeElement));

			if (rootMethods.contains(method))
			{
				continue;
			}

			if (superinterfacesMethods.contains(method))
			{
				final Method existingMethod = superinterfacesMethods.get(superinterfacesMethods.indexOf(method));

				if (!existingMethod.stateStrategy.equals(method.stateStrategy))
				{
					throw new IllegalStateException("Both " + existingMethod.enclosedClass + " and " + method.enclosedClass + " has method " + method.name + "(" + method.arguments.toString().substring(1, method.arguments.toString().length() - 1) + ") with difference strategies. Override this method in " + mViewClassName + " or make strategies equals");
				}
				if (!existingMethod.tag.equals(method.tag))
				{
					throw new IllegalStateException("Both " + existingMethod.enclosedClass + " and " + method.enclosedClass + " has method " + method.name + "(" + method.arguments.toString().substring(1, method.arguments.toString().length() - 1) + ") with difference tags. Override this method in " + mViewClassName + " or make tags equals");
				}

				continue;
			}

			superinterfacesMethods.add(method);
		}

		return superinterfacesMethods;
	}

	private String getClassName(TypeElement typeElement)
	{
		return typeElement.toString().substring(typeElement.toString().lastIndexOf(".") + 1);
	}

	private String generateLocalViewCommand(String viewClassName, String builder, List<Method> methods)
	{
		builder += "\tprivate enum LocalViewCommand implements ViewCommand<" + viewClassName + ">\n" +
				"\t{\n";

		boolean isFirstEnum = true;
		for (Method method : methods)
		{
			String argumentsString = "";
			for (Argument argument : method.arguments)
			{
				if (argumentsString.length() > 0)
				{
					argumentsString += ", ";
				}

				argumentsString += "params." + argument.name;
			}

			String generics = "";
			if (method.genericsCount > 0)
			{
				generics += '<';
				for (int i = 0; i < method.genericsCount; i++)
				{
					if (generics.length() > 1)
					{
						generics += ", ";
					}
					generics += '?';
				}
				generics += '>';
			}

			if (!isFirstEnum)
			{
				builder += ",\n";
			}
			isFirstEnum = false;

			builder += "\t\t" + method.uniqueName + "(" + method.stateStrategy + ", " + method.tag + ")\n" +
					"\t\t\t\t{\n" +
					"\t\t\t\t\t@Override\n" +
					"\t\t\t\t\tpublic void apply(" + viewClassName + " mvpView, Object paramsObject)\n" +
					"\t\t\t\t\t{\n" +
					(
							method.arguments.isEmpty() ?
									""
									:
									"\t\t\t\t\t\tfinal " + method.paramsClassName + generics + " params = (" + method.paramsClassName + ") paramsObject;\n"
					) +
					"\t\t\t\t\t\tmvpView." + method.name + "(" + argumentsString + ");\n" +
					"\t\t\t\t\t}\n" +
					"\t\t\t\t}";
		}

		builder += ";\n" +
				"\n" +
				"\t\tprivate Class<? extends StateStrategy> mStateStrategyType;\n" +
				"\t\tprivate String mTag;\n" +
				"\n" +
				"\t\tLocalViewCommand(Class<? extends StateStrategy> stateStrategyType, String tag)\n" +
				"\t\t{\n" +
				"\t\t\tmStateStrategyType = stateStrategyType;\n" +
				"\t\t\tmTag = tag;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\t@Override\n" +
				"\t\tpublic Class<? extends StateStrategy> getStrategyType()\n" +
				"\t\t{\n" +
				"\t\t\treturn mStateStrategyType;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\t@Override\n" +
				"\t\tpublic String getTag()\n" +
				"\t\t{\n" +
				"\t\t\treturn mTag;\n" +
				"\t\t}\n" +
				"\t}\n";

		for (Method method : methods)
		{
			if (method.arguments.isEmpty())
			{
				continue;
			}

			String argumentsInit = "";
			String argumentsBind = "";
			for (Argument argument : method.arguments)
			{
				argumentsInit += "\t\t" + argument.type + " " + argument.name + ";\n";
				argumentsBind += "\t\t\tthis." + argument.name + " = " + argument.name + ";\n";
			}

			builder += "\n\tprivate class " + method.paramsClassName + method.genericType + "\n" +
					"\t{\n" +
					argumentsInit +
					"\n" +
					"\t\t" + method.paramsClassName + "(" + join(", ", method.arguments) + ")\n" +
					"\t\t{\n" +
					argumentsBind +
					"\t\t}\n" +
					"\t}\n";
		}
		return builder;
	}

	public String getStateStrategyType(TypeElement typeElement)
	{
		String strategyClass = DEFAULT_STATE_STRATEGY;
		for (AnnotationMirror annotationMirror : typeElement.getAnnotationMirrors())
		{
			if (!annotationMirror.getAnnotationType().asElement().toString().equals(STATE_STRATEGY_TYPE_ANNOTATION))
			{
				continue;
			}

			final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
			final Set<? extends ExecutableElement> keySet = elementValues.keySet();

			for (ExecutableElement key : keySet)
			{
				if ("value()".equals(key.toString()))
				{
					strategyClass = elementValues.get(key).toString();
				}
			}
		}

		return strategyClass;
	}

	private static class Method
	{
		private int genericsCount; // add <?> to instance declaration
		String genericType;
		String resultType;
		String name;
		String uniqueName; // required for methods with same name but difference params
		String paramsClassName;
		List<Argument> arguments;
		List<String> thrownTypes;
		String stateStrategy;
		String tag;
		String enclosedClass;

		Method(int genericsCount, String genericType, String resultType, String name, List<Argument> arguments, List<String> thrownTypes, String stateStrategy, String methodTag, String enclosedClass)
		{
			this.genericsCount = genericsCount;
			this.genericType = genericType;
			this.resultType = resultType;
			this.name = name;
			this.arguments = arguments;
			this.thrownTypes = thrownTypes;
			this.stateStrategy = stateStrategy;
			this.tag = methodTag;
			this.enclosedClass = enclosedClass;
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Method method = (Method) o;

			//noinspection SimplifiableIfStatement
			if (name != null ? !name.equals(method.name) : method.name != null) return false;
			return !(arguments != null ? !arguments.equals(method.arguments) : method.arguments != null);

		}

		@Override
		public int hashCode()
		{
			int result = name != null ? name.hashCode() : 0;
			result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
			return result;
		}

		@Override
		public String toString()
		{
			return "Method{ " + genericType + ' ' + resultType + ' ' + name + '(' + arguments + ") throws " + thrownTypes + '}';
		}
	}

	private static class Argument
	{
		String type;
		String name;

		public Argument(String type, String name)
		{
			this.type = type;
			this.name = name;
		}

		@Override
		public String toString()
		{
			return type + " " + name;
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Argument argument = (Argument) o;

			return !(type != null ? !type.equals(argument.type) : argument.type != null);
		}

		@Override
		public int hashCode()
		{
			return type != null ? type.hashCode() : 0;
		}
	}

	public static String join(CharSequence delimiter, Iterable tokens)
	{
		StringBuilder sb = new StringBuilder();
		boolean firstTime = true;
		for (Object token : tokens)
		{
			if (firstTime)
			{
				firstTime = false;
			}
			else
			{
				sb.append(delimiter);
			}
			sb.append(token);
		}
		return sb.toString();
	}
}
