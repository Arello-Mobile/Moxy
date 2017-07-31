package com.arellomobile.mvp.compiler.viewstate;

import com.arellomobile.mvp.compiler.ElementProcessor;
import com.arellomobile.mvp.compiler.MvpCompiler;
import com.arellomobile.mvp.compiler.Util;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * Date: 27-Jul-2017
 * Time: 13:09
 *
 * @author Evgeny Kursakov
 */
public class ViewInterfaceProcessor extends ElementProcessor<TypeElement, ViewInterfaceInfo> {
	private static final String STATE_STRATEGY_TYPE_ANNOTATION = StateStrategyType.class.getName();
	private static final TypeElement DEFAULT_STATE_STRATEGY = MvpCompiler.getElementUtils().getTypeElement(AddToEndStrategy.class.getCanonicalName());

	private String mViewClassName;
	private Set<TypeElement> mStrategyClasses = new HashSet<>();

	public List<TypeElement> getStrategyClasses() {
		return new ArrayList<>(mStrategyClasses);
	}

	@Override
	public ViewInterfaceInfo process(TypeElement element) {
		ClassName viewName = ClassName.get(element);
		mViewClassName = viewName.toString();

		List<ViewMethod> methods = new ArrayList<>();

		TypeElement interfaceStateStrategyType = getInterfaceStateStrategyType(element);

		// Get methods for input class
		getMethods(element, interfaceStateStrategyType, new ArrayList<>(), methods);

		// Add methods from super interfaces
		methods.addAll(iterateInterfaces(0, element, interfaceStateStrategyType, methods, new ArrayList<>()));

		// Allow methods be with same names
		Map<String, Integer> methodsCounter = new HashMap<>();
		for (ViewMethod method : methods) {
			Integer counter = methodsCounter.get(method.getName());

			if (counter != null && counter > 0) {
				method.setUniqueSuffix(String.valueOf(counter));
			} else {
				counter = 0;
			}

			counter++;
			methodsCounter.put(method.getName(), counter);
		}

		List<TypeVariableName> typeVariables = element.getTypeParameters().stream()
				.map(TypeVariableName::get)
				.collect(Collectors.toList());

		return new ViewInterfaceInfo(viewName, typeVariables, methods);
	}


	private List<ViewMethod> getMethods(TypeElement typeElement,
	                                    TypeElement defaultStrategy,
	                                    List<ViewMethod> rootMethods,
	                                    List<ViewMethod> superinterfacesMethods) {
		for (Element element : typeElement.getEnclosedElements()) {
			if (element.getKind() != ElementKind.METHOD) {
				continue;
			}

			final ExecutableElement methodElement = (ExecutableElement) element;

			if (methodElement.getReturnType().getKind() != TypeKind.VOID) {
				MvpCompiler.getMessager().printMessage(Diagnostic.Kind.ERROR, "You are trying generate ViewState for " + typeElement.getSimpleName() + ". But " + typeElement.getSimpleName() + " contains non-void method \"" + methodElement.getSimpleName() + "\" that return type is " + methodElement.getReturnType() + ". See more here: https://github.com/Arello-Mobile/Moxy/issues/2");
			}

			AnnotationMirror annotation = Util.getAnnotation(methodElement, STATE_STRATEGY_TYPE_ANNOTATION);

			TypeMirror strategyClassFromAnnotation = Util.getAnnotationValueAsTypeMirror(annotation, "value");
			String tagFromAnnotation = Util.getAnnotationValueAsString(annotation, "tag");

			TypeElement strategyClass;
			if (strategyClassFromAnnotation != null) {
				strategyClass = (TypeElement) ((DeclaredType) strategyClassFromAnnotation).asElement();
			} else {
				strategyClass = defaultStrategy != null ? defaultStrategy : DEFAULT_STATE_STRATEGY;
			}

			String methodTag;
			if (tagFromAnnotation != null) {
				methodTag = tagFromAnnotation;
			} else {
				methodTag = methodElement.getSimpleName().toString();
			}

			mStrategyClasses.add(strategyClass);

			final ViewMethod method = new ViewMethod(methodElement, strategyClass, methodTag);

			if (rootMethods.contains(method)) {
				continue;
			}

			if (superinterfacesMethods.contains(method)) {
				final ViewMethod existingMethod = superinterfacesMethods.get(superinterfacesMethods.indexOf(method));

				if (!existingMethod.getStrategy().equals(method.getStrategy())) {
					throw new IllegalStateException("Both " + existingMethod.getEnclosedClassName() +
							" and " + method.getEnclosedClassName() +
							" has method " + method.getName() + "(" + method.getParameterSpecs().toString().substring(1, method.getParameterSpecs().toString().length() - 1) + ")" +
							" with difference strategies." +
							" Override this method in " + mViewClassName + " or make strategies equals");
				}
				if (!existingMethod.getTag().equals(method.getTag())) {
					throw new IllegalStateException("Both " + existingMethod.getEnclosedClassName() +
							" and " + method.getEnclosedClassName() +
							" has method " + method.getName() + "(" + method.getParameterSpecs().toString().substring(1, method.getParameterSpecs().toString().length() - 1) + ")" +
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
	                                           TypeElement parentDefaultStrategy,
	                                           List<ViewMethod> rootMethods,
	                                           List<ViewMethod> superinterfacesMethods) {
		for (TypeMirror typeMirror : parentElement.getInterfaces()) {
			final TypeElement anInterface = (TypeElement) ((DeclaredType) typeMirror).asElement();

			final List<? extends TypeMirror> typeArguments = ((DeclaredType) typeMirror).getTypeArguments();
			final List<? extends TypeParameterElement> typeParameters = anInterface.getTypeParameters();

			if (typeArguments.size() > typeParameters.size()) {
				throw new IllegalArgumentException("Code generation for interface " + anInterface.getSimpleName() + " failed. Simplify your generics.");
			}

			TypeElement defaultStrategy = parentDefaultStrategy != null ? parentDefaultStrategy : getInterfaceStateStrategyType(anInterface);

			getMethods(anInterface, defaultStrategy, rootMethods, superinterfacesMethods);

			iterateInterfaces(level + 1, anInterface, defaultStrategy, rootMethods, superinterfacesMethods);
		}

		return superinterfacesMethods;
	}

	private TypeElement getInterfaceStateStrategyType(TypeElement typeElement) {
		AnnotationMirror annotation = Util.getAnnotation(typeElement, STATE_STRATEGY_TYPE_ANNOTATION);
		TypeMirror value = Util.getAnnotationValueAsTypeMirror(annotation, "value");
		if (value != null && value.getKind() == TypeKind.DECLARED) {
			return (TypeElement) ((DeclaredType) value).asElement();
		} else {
			return null;
		}
	}
}
