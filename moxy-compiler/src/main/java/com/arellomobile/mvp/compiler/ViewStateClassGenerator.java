package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.arellomobile.mvp.compiler.Util.decapitalizeString;
import static com.arellomobile.mvp.compiler.Util.join;

/**
 * Date: 18.12.2015
 * Time: 13:24
 *
 * @author Yuri Shmakov
 */
final class ViewStateClassGenerator extends ClassGenerator<TypeElement> {
	private static final String STATE_STRATEGY_TYPE_ANNOTATION = StateStrategyType.class.getName();
	private static final TypeName DEFAULT_STATE_STRATEGY = TypeName.get(AddToEndStrategy.class);

	private String mViewClassName;
	private Set<String> mStrategyClasses;

	public ViewStateClassGenerator() {
		mStrategyClasses = new HashSet<>();
	}

	public Set<String> getStrategyClasses() {
		return mStrategyClasses;
	}

	public boolean generate(TypeElement typeElement, List<ClassGeneratingParams> classGeneratingParamsList) {
		TypeName viewTypeName = TypeName.get(typeElement.asType());
		mViewClassName = viewTypeName.toString();

		List<ViewMethod> methods = new ArrayList<>();

		TypeName interfaceStateStrategyType = getInterfaceStateStrategyType(typeElement);

		// Get methods for input class
		getMethods(typeElement, interfaceStateStrategyType, new ArrayList<>(), methods);

		// Add methods from super interfaces
		methods.addAll(iterateInterfaces(0, typeElement, interfaceStateStrategyType, methods, new ArrayList<>()));

		// Allow methods be with same names
		Map<String, Integer> methodsCounter = new HashMap<>();
		for (ViewMethod method : methods) {
			Integer counter = methodsCounter.get(method.name);

			if (counter != null && counter > 0) {
				method.uniqueName = method.name + counter;
			} else {
				counter = 0;
			}

			counter++;
			methodsCounter.put(method.name, counter);
		}

		ClassName viewClassName = ClassName.get(typeElement);
		String viewSimpleName = join("$", viewClassName.simpleNames());
		String packageName = viewClassName.packageName();

		List<TypeVariableName> typeVariables = new ArrayList<>(typeElement.getTypeParameters().size());
		for (TypeParameterElement typeName : typeElement.getTypeParameters()) {
			typeVariables.add(TypeVariableName.get(typeName));
		}

		JavaFile javaFile = generateFile(viewTypeName, viewSimpleName, packageName, typeVariables, methods);

		ClassGeneratingParams classGeneratingParams = new ClassGeneratingParams();
		classGeneratingParams.setName(viewClassName.packageName() + "." + viewSimpleName + MvpProcessor.VIEW_STATE_SUFFIX);
		classGeneratingParams.setBody(javaFile.toString());
		classGeneratingParamsList.add(classGeneratingParams);

		return true;
	}

	private List<ViewMethod> getMethods(TypeElement typeElement,
	                                    TypeName defaultStrategy,
	                                    List<ViewMethod> rootMethods,
	                                    List<ViewMethod> superinterfacesMethods) {
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

			TypeName strategyClass = defaultStrategy != null ? defaultStrategy : DEFAULT_STATE_STRATEGY;
			String methodTag = methodElement.getSimpleName().toString();
			for (AnnotationMirror annotationMirror : methodElement.getAnnotationMirrors()) {
				if (annotationMirror.getAnnotationType().asElement().toString().equals(STATE_STRATEGY_TYPE_ANNOTATION)) {
					strategyClass = TypeName.get(Util.getAnnotationValueAsType(annotationMirror, "value"));
					methodTag = Util.getAnnotationValue(annotationMirror, "tag").toString();
				}
			}

			mStrategyClasses.add(strategyClass.toString() + ".class");

			final ViewMethod method = new ViewMethod(methodElement, strategyClass, methodTag);

			if (rootMethods.contains(method)) {
				continue;
			}

			if (superinterfacesMethods.contains(method)) {
				final ViewMethod existingMethod = superinterfacesMethods.get(superinterfacesMethods.indexOf(method));

				if (!existingMethod.strategyClassName.equals(method.strategyClassName)) {
					throw new IllegalStateException("Both " + existingMethod.getEnclosedClassName() +
							" and " + method.getEnclosedClassName() +
							" has method " + method.name + "(" + method.parameterSpecs.toString().substring(1, method.parameterSpecs.toString().length() - 1) + ")" +
							" with difference strategies." +
							" Override this method in " + mViewClassName + " or make strategies equals");
				}
				if (!existingMethod.tag.equals(method.tag)) {
					throw new IllegalStateException("Both " + existingMethod.getEnclosedClassName() +
							" and " + method.getEnclosedClassName() +
							" has method " + method.name + "(" + method.parameterSpecs.toString().substring(1, method.parameterSpecs.toString().length() - 1) + ")" +
							" with difference tags." +
							" Override this method in " + mViewClassName + " or make tags equals");
				}

				continue;
			}

			superinterfacesMethods.add(method);
		}

		return superinterfacesMethods;
	}

	private List<ViewMethod> iterateInterfaces(int level,
	                                           TypeElement parentElement,
	                                           TypeName parentDefaultStrategy,
	                                           List<ViewMethod> rootMethods,
	                                           List<ViewMethod> superinterfacesMethods) {
		for (TypeMirror typeMirror : parentElement.getInterfaces()) {
			final TypeElement anInterface = (TypeElement) ((DeclaredType) typeMirror).asElement();

			final List<? extends TypeMirror> typeArguments = ((DeclaredType) typeMirror).getTypeArguments();
			final List<? extends TypeParameterElement> typeParameters = anInterface.getTypeParameters();

			if (typeArguments.size() > typeParameters.size()) {
				throw new IllegalArgumentException("Code generation for interface " + anInterface.getSimpleName() + " failed. Simplify your generics.");
			}

			TypeName defaultStrategy = parentDefaultStrategy != null ? parentDefaultStrategy : getInterfaceStateStrategyType(anInterface);

			getMethods(anInterface, defaultStrategy, rootMethods, superinterfacesMethods);

			iterateInterfaces(level + 1, anInterface, defaultStrategy, rootMethods, superinterfacesMethods);
		}

		return superinterfacesMethods;
	}

	private TypeName getInterfaceStateStrategyType(TypeElement typeElement) {
		for (AnnotationMirror annotationMirror : typeElement.getAnnotationMirrors()) {
			if (annotationMirror.getAnnotationType().asElement().toString().equals(STATE_STRATEGY_TYPE_ANNOTATION)) {
				return TypeName.get(Util.getAnnotationValueAsType(annotationMirror, "value"));
			}
		}

		return null;
	}


	private JavaFile generateFile(TypeName viewTypeName,
	                              String viewName,
	                              String packageName,
	                              List<TypeVariableName> typeVariables,
	                              List<ViewMethod> methods) {
		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(viewName + MvpProcessor.VIEW_STATE_SUFFIX)
				.addModifiers(Modifier.PUBLIC)
				.superclass(ParameterizedTypeName.get(ClassName.get(MvpViewState.class), viewTypeName))
				.addSuperinterface(viewTypeName)
				.addTypeVariables(typeVariables);

		for (ViewMethod method : methods) {
			TypeSpec commandClass = generateCommandClass(method, viewTypeName);
			classBuilder.addType(commandClass);
			classBuilder.addMethod(generateMethod(method, viewTypeName, commandClass));
		}

		return JavaFile.builder(packageName, classBuilder.build())
				.indent("\t")
				.build();
	}

	private TypeSpec generateCommandClass(ViewMethod method, TypeName viewTypeName) {
		String argumentsString = method.parameterSpecs.stream()
				.map(parameterSpec -> parameterSpec.name)
				.collect(Collectors.joining(", "));

		MethodSpec applyMethod = MethodSpec.methodBuilder("apply")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(viewTypeName, "mvpView")
				.addExceptions(method.exceptions)
				.addStatement("mvpView.$L($L)", method.name, argumentsString)
				.build();

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(method.getCommandClassName())
				.superclass(ParameterizedTypeName.get(ClassName.get(ViewCommand.class), viewTypeName))
				.addMethod(generateCommandConstructor(method))
				.addMethod(applyMethod);

		for (ParameterSpec parameter : method.parameterSpecs) {
			classBuilder.addField(parameter.type, parameter.name, Modifier.PUBLIC, Modifier.FINAL);
		}

		return classBuilder.build();
	}

	private MethodSpec generateMethod(ViewMethod method, TypeName viewTypeName, TypeSpec commandClass) {
		List<String> argumentNames = new ArrayList<>();
		for (ParameterSpec parameter : method.parameterSpecs) {
			argumentNames.add(parameter.name);
		}
		String argumentsString = join(", ", argumentNames);

		String commandFieldName = decapitalizeString(method.getCommandClassName());

		// Add salt if contains argument with same name
		Random random = new Random();
		while (argumentNames.contains(commandFieldName)) {
			commandFieldName += random.nextInt(10);
		}

		return MethodSpec.overriding(method.element)
				.addStatement("$1N $2L = new $1N($3L)", commandClass, commandFieldName, argumentsString)
				.addStatement("mViewCommands.beforeApply($L)", commandFieldName)
				.addCode("\n")
				.beginControlFlow("if (mViews == null || mViews.isEmpty())")
				.addStatement("return")
				.endControlFlow()
				.addCode("\n")
				.beginControlFlow("for($T view : mViews)", viewTypeName)
				.addStatement("view.$L($L)", method.name, argumentsString)
				.endControlFlow()
				.addCode("\n")
				.addStatement("mViewCommands.afterApply($L)", commandFieldName)
				.build();
	}

	private MethodSpec generateCommandConstructor(ViewMethod method) {
		List<ParameterSpec> parameters = method.parameterSpecs;

		MethodSpec.Builder builder = MethodSpec.constructorBuilder()
				.addParameters(parameters)
				.addStatement("super($S, $T.class)", method.tag, method.strategyClassName);

		if (parameters.size() > 0) {
			builder.addCode("\n");
		}

		for (ParameterSpec parameter : parameters) {
			builder.addStatement("this.$1N = $1N", parameter);
		}

		return builder.build();
	}

	private static class ViewMethod {
		final ExecutableElement element;
		final String name;
		final TypeName strategyClassName;
		final String tag;
		final List<ParameterSpec> parameterSpecs;
		final List<TypeName> exceptions;

		String uniqueName;

		ViewMethod(ExecutableElement methodElement,
		           TypeName strategyClassName,
		           String tag) {
			this.element = methodElement;
			this.strategyClassName = strategyClassName;
			this.tag = tag;
			this.name = methodElement.getSimpleName().toString();

			this.parameterSpecs = methodElement.getParameters()
					.stream()
					.map(ParameterSpec::get)
					.collect(Collectors.toList());

			this.exceptions = element.getThrownTypes().stream()
					.map(TypeName::get)
					.collect(Collectors.toList());

			this.uniqueName = this.name;
		}

		String getCommandClassName() {
			return uniqueName.substring(0, 1).toUpperCase() + uniqueName.substring(1) + "Command";
		}

		String getEnclosedClassName() {
			TypeElement typeElement = (TypeElement) element.getEnclosingElement();
			return typeElement.getQualifiedName().toString();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			ViewMethod that = (ViewMethod) o;

			return element.equals(that.element);

		}

		@Override
		public int hashCode() {
			return element.hashCode();
		}
	}
}
