package com.arellomobile.mvp.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic;


import static com.arellomobile.mvp.compiler.Util.fillGenerics;

/**
 * Date: 18.12.2015
 * Time: 13:24
 *
 * @author Yuri Shmakov
 */
final class ViewStateClassGenerator extends ClassGenerator<TypeElement> {
	public static final String STATE_STRATEGY_TYPE_ANNOTATION = StateStrategyType.class.getName();
	public static final String DEFAULT_STATE_STRATEGY = AddToEndStrategy.class.getName() + ".class";
	private static final String DEFAULT_STATE_STRATEGY_OPTION = "defaultStateStrategy";

	private String mViewClassName;
	private Set<String> mStrategyClasses;

	public ViewStateClassGenerator() {
		mStrategyClasses = new HashSet<>();
	}

	public boolean generate(TypeElement typeElement, List<ClassGeneratingParams> classGeneratingParamsList) {
		String generic = Util.getClassGenerics(typeElement);
		String interfaceGeneric = "";
		if (!typeElement.getTypeParameters().isEmpty()) {
			interfaceGeneric = "<" + join(",", typeElement.getTypeParameters()) + ">";
		}

		String fullClassName = Util.getFullClassName(typeElement);

		ClassGeneratingParams classGeneratingParams = new ClassGeneratingParams();
		classGeneratingParams.setName(fullClassName + MvpProcessor.VIEW_STATE_SUFFIX);

		mViewClassName = getClassName(typeElement) + interfaceGeneric;

		String builder = "package " + fullClassName.substring(0, fullClassName.lastIndexOf(".")) + ";\n" +
		                 "\n" +
		                 "import java.util.Set;\n" +
		                 "\n" +
		                 "import com.arellomobile.mvp.viewstate.MvpViewState;\n" +
		                 "import com.arellomobile.mvp.viewstate.ViewCommand;\n" +
		                 "import com.arellomobile.mvp.viewstate.ViewCommands;\n" +
		                 "import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;\n" +
		                 "import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;\n" +
		                 "import com.arellomobile.mvp.viewstate.strategy.StateStrategy;\n" +
		                 "\n" +
		                 "public class " + fullClassName.substring(fullClassName.lastIndexOf(".") + 1) + "$$State" + generic + " extends MvpViewState<" + mViewClassName + "> implements " + mViewClassName + " {\n" +
		                 "\n";

		List<Method> methods = new ArrayList<>();

		String stateStrategyType = getStateStrategyType(typeElement);

		Map<String, String> types = new HashMap<>();
		if (!typeElement.getTypeParameters().isEmpty()) {
			for (TypeParameterElement typeParameterElement : typeElement.getTypeParameters()) {
				types.put(typeParameterElement.toString(), typeParameterElement.toString());
			}
		}

		// Get methods for input class
		getMethods(types, typeElement, stateStrategyType, new ArrayList<Method>(), methods);

		// Add methods from super intefaces
		methods.addAll(iterateInterfaces(0, typeElement, stateStrategyType, new HashMap<String, String>(), methods, new ArrayList<Method>()));

		// Allow methods be with same names
		Map<String, Integer> methodsCounter = new HashMap<>();
		for (Method method : methods) {
			Integer counter = methodsCounter.get(method.name);

			if (counter == null || counter == 0) {
				counter = 0;
				method.uniqueName = method.name;
			} else {
				method.uniqueName = method.name + counter;
			}

			method.commandClassName = method.uniqueName.substring(0, 1).toUpperCase() + method.uniqueName.substring(1) + "Command";

			counter++;
			methodsCounter.put(method.name, counter);
		}

		for (Method method : methods) {
			String throwTypesString = join(", ", method.thrownTypes);
			if (throwTypesString.length() > 0) {
				throwTypesString = " throws " + throwTypesString;
			}

			String fieldName = "params";
			String argumentsString = "";
			List<String> argumentNames = new ArrayList<>();

			int index = 0;
			for (Argument argument : method.arguments) {
				if (argument.name.equals(fieldName)) {
					fieldName = "params" + index;
				}

				if (argumentsString.length() > 0) {
					argumentsString += ", ";
				}
				argumentsString += argument.name;
				argumentNames.add(argument.name);
				index++;
			}


			String commandFieldName = decapitalizeString(method.commandClassName);
			String commandClassName = method.commandClassName;
			String commandWrapperNewInstance = "new " + method.commandClassName + "(" + argumentsString + ");\n";

			// Add salt if contains argument with same name
			Random random = new Random();
			while (argumentNames.contains(commandFieldName)) {
				commandFieldName += random.nextInt(10);
			}

			builder += "\t@Override\n" +
			           "\tpublic " + method.genericType + " void " + method.name + "(" + join(", ", method.arguments) + ")" + throwTypesString + " {\n" +
			           "\t\t" + commandClassName + " " + commandFieldName + " = " + commandWrapperNewInstance +
			           "\t\tmViewCommands.beforeApply(" + commandFieldName + ");\n" +
			           "\n" +
			           "\t\tif (mViews == null || mViews.isEmpty()) {\n" +
			           "\t\t\treturn;\n" +
			           "\t\t}\n" +
			           "\n" +
			           "\t\tfor(" + mViewClassName + " view : mViews) {\n" +
			           "\t\t\tview." + method.name + "(" + argumentsString + ");\n" +
			           "\t\t}\n" +
			           "\n" +
			           "\t\tmViewCommands.afterApply(" + commandFieldName + ");\n" +
			           "\t}\n" +
			           "\n";
		}

		if (!methods.isEmpty()) {
			builder = generateLocalViewCommand(mViewClassName, builder, methods);
		}

		builder += "}\n";

		classGeneratingParams.setBody(builder);
		classGeneratingParamsList.add(classGeneratingParams);

		return true;
	}

	private List<Method> iterateInterfaces(int level, TypeElement parentElement, String parentDefaultStrategy, Map<String, String> parentTypes, List<Method> rootMethods, List<Method> superinterfacesMethods) {
		for (TypeMirror typeMirror : parentElement.getInterfaces()) {
			final TypeElement anInterface = (TypeElement) ((DeclaredType) typeMirror).asElement();

			final List<? extends TypeMirror> typeArguments = ((DeclaredType) typeMirror).getTypeArguments();
			final List<? extends TypeParameterElement> typeParameters = anInterface.getTypeParameters();

			if (typeArguments.size() > typeParameters.size()) {
				throw new IllegalArgumentException("Code generation for interface " + anInterface.getSimpleName() + " failed. Simplify your generics.");
			}

			Map<String, String> types = new HashMap<>();
			for (int i = 0; i < typeArguments.size(); i++) {
				types.put(typeParameters.get(i).toString(), typeArguments.get(i).toString());
			}

			Map<String, String> totalInterfaceTypes = new HashMap<>(typeParameters.size());
			for (int i = 0; i < typeArguments.size(); i++) {
				totalInterfaceTypes.put(typeParameters.get(i).toString(), fillGenerics(parentTypes, typeArguments.get(i)));
			}
			for (int i = typeArguments.size(); i < typeParameters.size(); i++) {
				if (typeParameters.get(i).getBounds().size() != 1) {
					throw new IllegalArgumentException("Code generation for interface " + anInterface.getSimpleName() + " failed. Simplify your generics.");
				}

				totalInterfaceTypes.put(typeParameters.get(i).toString(), typeParameters.get(i).getBounds().get(0).toString());
			}

			String defaultStrategy = parentDefaultStrategy != null ? parentDefaultStrategy : getStateStrategyType(anInterface);

			getMethods(totalInterfaceTypes, anInterface, defaultStrategy, rootMethods, superinterfacesMethods);

			iterateInterfaces(level + 1, anInterface, defaultStrategy, types, rootMethods, superinterfacesMethods);
		}

		return superinterfacesMethods;
	}

	private List<Method> getMethods(Map<String, String> types, TypeElement typeElement, String defaultStrategy, List<Method> rootMethods, List<Method> superinterfacesMethods) {
		for (Element element : typeElement.getEnclosedElements()) {
			if (!(element instanceof ExecutableElement)) {
				continue;
			}

			final ExecutableElement methodElement = (ExecutableElement) element;

			if (methodElement.getModifiers().contains(Modifier.STATIC)) {
				continue;
			}

			if (!methodElement.getReturnType().toString().equals("void")) {
				MvpCompiler.getMessager().printMessage(Diagnostic.Kind.ERROR, "You are trying generate ViewState for " + typeElement.getSimpleName() + ". But " + typeElement.getSimpleName() + " contains non-void method \"" + methodElement.getSimpleName() + "\" that return type is " + methodElement.getReturnType() + ". See more here: https://github.com/Arello-Mobile/Moxy/issues/2");
			}

			String strategyClass = defaultStrategy != null ? defaultStrategy : getDefaultStateStrategy();
			String methodTag = "\"" + methodElement.getSimpleName() + "\"";
			for (AnnotationMirror annotationMirror : methodElement.getAnnotationMirrors()) {
				if (!annotationMirror.getAnnotationType().asElement().toString().equals(STATE_STRATEGY_TYPE_ANNOTATION)) {
					continue;
				}

				final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
				final Set<? extends ExecutableElement> keySet = elementValues.keySet();

				for (ExecutableElement executableElement : keySet) {
					String key = executableElement.getSimpleName().toString();

					if ("value".equals(key)) {
						strategyClass = elementValues.get(executableElement).toString();
					} else if ("tag".equals(key)) {
						methodTag = elementValues.get(executableElement).toString();
					}
				}
			}

			Map<String, String> methodTypes = new HashMap<>(types);

			final ExecutableType executableType = (ExecutableType) methodElement.asType();
			final List<? extends TypeVariable> typeVariables = executableType.getTypeVariables();
			if (!typeVariables.isEmpty()) {
				for (TypeVariable typeVariable : typeVariables) {
					methodTypes.put(typeVariable.asElement().toString(), typeVariable.asElement().toString());
				}
			}

			String generics = "";

			if (!typeVariables.isEmpty()) {
				generics += "<";
				for (TypeVariable typeVariable : typeVariables) {
					if (generics.length() > 1) {
						generics += ", ";
					}

					final TypeMirror upperBound = typeVariable.getUpperBound();

					if (upperBound.toString().equals(Object.class.getCanonicalName())) {
						generics += typeVariable.asElement();
						continue;
					}

					final String filledGeneric = fillGenerics(methodTypes, upperBound);
					if (filledGeneric.startsWith("?")) {
						generics += filledGeneric.replaceFirst("\\?", typeVariable.asElement().toString());
					} else {
						generics += typeVariable.asElement() + " extends " + filledGeneric;
					}
				}
				generics += "> ";
			}

			final List<? extends VariableElement> parameters = methodElement.getParameters();

			List<Argument> arguments = new ArrayList<>();
			for (VariableElement parameter : parameters) {
				arguments.add(new Argument(fillGenerics(methodTypes, parameter.asType()), parameter.toString(), parameter.getAnnotationMirrors()));
			}

			List<String> throwTypes = new ArrayList<>();
			for (TypeMirror typeMirror : methodElement.getThrownTypes()) {
				throwTypes.add(fillGenerics(methodTypes, typeMirror));
			}

			mStrategyClasses.add(strategyClass);

			final Method method = new Method(generics, methodElement.getSimpleName().toString(), arguments, throwTypes, strategyClass, methodTag, getClassName(typeElement));

			if (rootMethods.contains(method)) {
				continue;
			}

			if (superinterfacesMethods.contains(method)) {
				final Method existingMethod = superinterfacesMethods.get(superinterfacesMethods.indexOf(method));

				if (!existingMethod.stateStrategy.equals(method.stateStrategy)) {
					throw new IllegalStateException("Both " + existingMethod.enclosedClass + " and " + method.enclosedClass + " has method " + method.name + "(" + method.arguments.toString().substring(1, method.arguments.toString().length() - 1) + ") with difference strategies. Override this method in " + mViewClassName + " or make strategies equals");
				}
				if (!existingMethod.tag.equals(method.tag)) {
					throw new IllegalStateException("Both " + existingMethod.enclosedClass + " and " + method.enclosedClass + " has method " + method.name + "(" + method.arguments.toString().substring(1, method.arguments.toString().length() - 1) + ") with difference tags. Override this method in " + mViewClassName + " or make tags equals");
				}

				continue;
			}

			superinterfacesMethods.add(method);
		}

		return superinterfacesMethods;
	}

	private String getClassName(TypeElement typeElement) {
		return typeElement.getQualifiedName().toString();
	}

	private String generateLocalViewCommand(String viewClassName, String builder, List<Method> methods) {
		for (Method method : methods) {
			String argumentsString = "";
			for (Argument argument : method.arguments) {
				if (argumentsString.length() > 0) {
					argumentsString += ", ";
				}

				argumentsString += argument.name;
			}

			String argumentsInit = "";
			String argumentsBind = "";
			for (Argument argument : method.arguments) {
				argumentsInit += "\t\tpublic final " + argument.type + " " + argument.name + ";\n";
				argumentsBind += "\t\t\tthis." + argument.name + " = " + argument.name + ";\n";
			}
			if (!argumentsInit.isEmpty()) {
				argumentsInit += "\n";
			}

			builder += "\n\tpublic class " + method.commandClassName + method.genericType + " extends ViewCommand<" + viewClassName + "> {\n" +
			           argumentsInit +
			           "\t\t" + method.commandClassName + "(" + join(", ", method.arguments) + ") {\n" +
			           "\t\t\tsuper(" + method.tag + ", " + method.stateStrategy + ");\n" +
			           argumentsBind +
			           "\t\t}\n" +
			           "\n" +
			           "\t\t@Override\n" +
			           "\t\tpublic void apply(" + viewClassName + " mvpView) {\n" +
			           "\t\t\tmvpView." + method.name + "(" + argumentsString + ");\n" +
			           "\t\t}\n" +
			           "\t}\n";
		}
		return builder;
	}

	private String getDefaultStateStrategy() {
		return DEFAULT_STATE_STRATEGY;
	}

	public String getStateStrategyType(TypeElement typeElement) {
		for (AnnotationMirror annotationMirror : typeElement.getAnnotationMirrors()) {
			if (!annotationMirror.getAnnotationType().asElement().toString().equals(STATE_STRATEGY_TYPE_ANNOTATION)) {
				continue;
			}

			final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
			final Set<? extends ExecutableElement> keySet = elementValues.keySet();

			for (ExecutableElement key : keySet) {
				if ("value".equals(key.getSimpleName().toString())) {
					return elementValues.get(key).toString();
				}
			}
		}

		return null;
	}

	private static class Method {
		String genericType;
		String name;
		String uniqueName; // required for methods with same name but difference params
		String commandClassName;
		List<Argument> arguments;
		List<String> thrownTypes;
		String stateStrategy;
		String tag;
		String enclosedClass;

		Method(String genericType, String name, List<Argument> arguments, List<String> thrownTypes, String stateStrategy, String methodTag, String enclosedClass) {
			this.genericType = genericType;
			this.name = name;
			this.arguments = arguments;
			this.thrownTypes = thrownTypes;
			this.stateStrategy = stateStrategy;
			this.tag = methodTag;
			this.enclosedClass = enclosedClass;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			Method method = (Method) o;

			//noinspection SimplifiableIfStatement
			if (name != null ? !name.equals(method.name) : method.name != null) {
				return false;
			}
			return !(arguments != null ? !arguments.equals(method.arguments) : method.arguments != null);

		}

		@Override
		public int hashCode() {
			int result = name != null ? name.hashCode() : 0;
			result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "Method { " + genericType + " void " + name + '(' + arguments + ") throws " + thrownTypes + '}';
		}
	}

	private static class Argument {
		String type;
		String name;
		List<? extends AnnotationMirror> annotations;

		public Argument(String type, String name, List<? extends AnnotationMirror> annotations) {
			this.type = type;
			this.name = name;
			this.annotations = annotations;
		}

		@Override
		public String toString() {
			return join(" ", annotations) + " " + type + " " + name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			Argument argument = (Argument) o;

			return !(type != null ? !type.equals(argument.type) : argument.type != null);
		}

		@Override
		public int hashCode() {
			return type != null ? type.hashCode() : 0;
		}
	}

	public static String join(CharSequence delimiter, Iterable tokens) {
		StringBuilder sb = new StringBuilder();
		boolean firstTime = true;
		for (Object token : tokens) {
			if (firstTime) {
				firstTime = false;
			} else {
				sb.append(delimiter);
			}
			sb.append(token);
		}
		return sb.toString();
	}

	public static String decapitalizeString(String string) {
		return string == null || string.isEmpty() ? "" : string.length() == 1 ? string.toLowerCase() : Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}

	public Set<String> getStrategyClasses() {
		return mStrategyClasses;
	}
}
