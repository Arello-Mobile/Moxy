package com.omegar.mvp.compiler.viewstate;

import com.omegar.mvp.compiler.ElementProcessor;
import com.omegar.mvp.compiler.MvpCompiler;
import com.omegar.mvp.compiler.Util;
import com.omegar.mvp.viewstate.strategy.AddToEndStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;
import com.squareup.javapoet.ParameterSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.omegar.mvp.compiler.Util.MVP_VIEW_CLASS_NAME;
import static com.omegar.mvp.compiler.Util.asElement;
import static com.omegar.mvp.compiler.Util.isMvpElement;

/**
 * Date: 27-Jul-2017
 * Time: 13:09
 *
 * @author Evgeny Kursakov
 */
public class ViewInterfaceProcessor extends ElementProcessor<TypeElement, List<ViewInterfaceInfo>> {
	private static final String STATE_STRATEGY_TYPE_ANNOTATION = StateStrategyType.class.getName();
	private static final TypeElement DEFAULT_STATE_STRATEGY = MvpCompiler.getElementUtils().getTypeElement(AddToEndStrategy.class.getCanonicalName());

	private TypeElement viewInterfaceElement;
	private String viewInterfaceName;
	private Set<TypeElement> usedStrategies = new HashSet<>();

	public List<TypeElement> getUsedStrategies() {
		return new ArrayList<>(usedStrategies);
	}

	@Override
	public List<ViewInterfaceInfo> process(TypeElement element) {
		List<ViewInterfaceInfo> list = new ArrayList<>(generateInfos(element));
		fillWithNotInheredMethods(list);
		return list;
	}

	private void fillWithNotInheredMethods(List<ViewInterfaceInfo> list) {
		for (ViewInterfaceInfo info : list) {
			List<ViewMethod> infoMethods = info.getMethods();

			if (info.getSuperTypeMvpElements().size() > 1) {
				List<ViewMethod> inheredMethods = getInheredMethods(info);
				Set<ViewMethod> notInheredMethods = getNotInheredMethods(info, list);
				for (ViewMethod method : notInheredMethods) {
					if (!inheredMethods.contains(method)) infoMethods.add(method);
				}
			}
		}
	}

	private List<ViewMethod> getInheredMethods(ViewInterfaceInfo info) {
		List<ViewMethod> methods = new ArrayList<>(info.getMethods());

		ViewInterfaceInfo superInterfaceInfo = info.getSuperInterfaceInfo();
		if (superInterfaceInfo != null) methods.addAll(getInheredMethods(superInterfaceInfo));

		return methods;
	}

	private Set<ViewMethod> getNotInheredMethods(ViewInterfaceInfo info, List<ViewInterfaceInfo> infoList) {
		if (info.getSuperTypeMvpElements().size() <= 1) return Collections.emptySet();

		assert info.getSuperInterfaceInfo() != null;
		TypeElement superClassElement = info.getSuperInterfaceInfo().getElement();

		Set<ViewMethod> methodSet = new LinkedHashSet<>();
		for (TypeElement element : info.getSuperTypeMvpElements()) {
			if (!element.equals(superClassElement)) {
				ViewInterfaceInfo infoByTypeElement = getViewInterfaceInfoByTypeElement(infoList, element);
				if (infoByTypeElement != null) {
					methodSet.addAll(getInheredMethods(infoByTypeElement));
					methodSet.addAll(getNotInheredMethods(infoByTypeElement, infoList));
				}
			}
		}
		return methodSet;
	}

	private ViewInterfaceInfo getViewInterfaceInfoByTypeElement(List<ViewInterfaceInfo> list, TypeElement element) {
		for (ViewInterfaceInfo info : list) {
			if (info.getElement().equals(element)) return info;
		}
		return null;
	}

	private Set<ViewInterfaceInfo> generateInfos(TypeElement element) {
		Set<ViewInterfaceInfo> interfaceInfos = new LinkedHashSet<>();
		this.viewInterfaceElement = element;
		viewInterfaceName = element.getSimpleName().toString();

		List<ViewMethod> methods = new ArrayList<>();

		TypeElement interfaceStateStrategyType = getInterfaceStateStrategyType(element);

		// Get methods for input class
		getMethods(element, interfaceStateStrategyType, new ArrayList<>(), methods);

        // Add methods from super interfaces
		ViewInterfaceInfo superInterfaceInfo = null;
		for (TypeMirror typeMirror : element.getInterfaces()) {
			final TypeElement interfaceElement = asElement(typeMirror);
			if (isMvpElement(interfaceElement)) {
				Set<ViewInterfaceInfo> parentInfos = generateInfos(interfaceElement);
				if (superInterfaceInfo == null) {
					superInterfaceInfo = Util.lastOrNull(parentInfos);
				}
				interfaceInfos.addAll(parentInfos);
            }
		}

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

		ViewInterfaceInfo info = new ViewInterfaceInfo(superInterfaceInfo, element, methods);
		if (!info.getName().equals(MVP_VIEW_CLASS_NAME)) interfaceInfos.add(info);

		return interfaceInfos;
	}

	private void getMethods(TypeElement typeElement,
	                        TypeElement defaultStrategy,
	                        List<ViewMethod> rootMethods,
	                        List<ViewMethod> superinterfacesMethods) {
		for (Element element : typeElement.getEnclosedElements()) {
			// ignore all but non-static methods
			if (element.getKind() != ElementKind.METHOD || element.getModifiers().contains(Modifier.STATIC)) {
				continue;
			}

			final ExecutableElement methodElement = (ExecutableElement) element;

			if (methodElement.getReturnType().getKind() != TypeKind.VOID) {
				String message = String.format("You are trying generate ViewState for %s. " +
								"But %s contains non-void method \"%s\" that return type is %s. " +
								"See more here: https://github.com/Arello-Mobile/Moxy/issues/2",
						typeElement.getSimpleName(),
						typeElement.getSimpleName(),
						methodElement.getSimpleName(),
						methodElement.getReturnType()
				);
				MvpCompiler.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
			}

			AnnotationMirror annotation = Util.getAnnotation(methodElement, STATE_STRATEGY_TYPE_ANNOTATION);

			// get strategy from annotation
			TypeMirror strategyClassFromAnnotation = Util.getAnnotationValueAsTypeMirror(annotation, "value");

			TypeElement strategyClass;
			if (strategyClassFromAnnotation != null) {
				strategyClass = (TypeElement) ((DeclaredType) strategyClassFromAnnotation).asElement();
			} else {
				strategyClass = defaultStrategy != null ? defaultStrategy : DEFAULT_STATE_STRATEGY;
			}

			// get tag from annotation
			String tagFromAnnotation = Util.getAnnotationValueAsString(annotation, "tag");

			String methodTag;
			if (tagFromAnnotation != null) {
				methodTag = tagFromAnnotation;
			} else {
				methodTag = methodElement.getSimpleName().toString();
			}

			// add strategy to list
			usedStrategies.add(strategyClass);

			final ViewMethod method = new ViewMethod(
					(DeclaredType) viewInterfaceElement.asType(), methodElement, strategyClass, methodTag
			);

			if (rootMethods.contains(method)) {
				continue;
			}

			if (superinterfacesMethods.contains(method)) {
				checkStrategyAndTagEquals(method, superinterfacesMethods.get(superinterfacesMethods.indexOf(method)));
				continue;
			}

			superinterfacesMethods.add(method);
		}
	}

	private void checkStrategyAndTagEquals(ViewMethod method, ViewMethod existingMethod) {
		List<String> differentParts = new ArrayList<>();
		if (!existingMethod.getStrategy().equals(method.getStrategy())) {
			differentParts.add("strategies");
		}
		if (!existingMethod.getTag().equals(method.getTag())) {
			differentParts.add("tags");
		}

		if (!differentParts.isEmpty()) {
			String arguments = method.getParameterSpecs().stream()
					.map(ParameterSpec::toString)
					.collect(Collectors.joining(", "));

			String parts = differentParts.stream().collect(Collectors.joining(" and "));

			throw new IllegalStateException("Both " + existingMethod.getEnclosedClassName() +
					" and " + method.getEnclosedClassName() +
					" has method " + method.getName() + "(" + arguments + ")" +
					" with different " + parts + "." +
					" Override this method in " + viewInterfaceName + " or make " + parts + " equals");
		}
	}

	private List<ViewMethod> iterateInterfaces(TypeElement parentElement,
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

			iterateInterfaces(anInterface, defaultStrategy, rootMethods, superinterfacesMethods);
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
