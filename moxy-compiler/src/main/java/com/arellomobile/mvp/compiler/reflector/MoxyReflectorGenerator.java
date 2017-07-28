package com.arellomobile.mvp.compiler.reflector;

import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.ViewStateProvider;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.arellomobile.mvp.compiler.MvpCompiler.MOXY_REFLECTOR_DEFAULT_PACKAGE;
import static com.arellomobile.mvp.compiler.Util.join;

/**
 * Date: 07.12.2016
 * Time: 19:05
 *
 * @author Yuri Shmakov
 */

public class MoxyReflectorGenerator {
	private static final Comparator<TypeElement> TYPE_ELEMENT_COMPARATOR
			= (e1, e2) -> e1.toString().compareTo(e2.toString());

	private static final TypeName CLASS_WILDCARD_TYPE_NAME
			= ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(TypeName.OBJECT));
	private static final ParameterizedTypeName LIST_OF_OBJECT_TYPE_NAME
			= ParameterizedTypeName.get(ClassName.get(List.class), TypeName.OBJECT);
	private static final ParameterizedTypeName MAP_CLASS_TO_OBJECT_TYPE_NAME
			= ParameterizedTypeName.get(ClassName.get(Map.class), CLASS_WILDCARD_TYPE_NAME, TypeName.OBJECT);
	private static final ParameterizedTypeName MAP_CLASS_TO_LIST_OF_OBJECT_TYPE_NAME
			= ParameterizedTypeName.get(ClassName.get(Map.class), CLASS_WILDCARD_TYPE_NAME, LIST_OF_OBJECT_TYPE_NAME);

	public static JavaFile generate(String destinationPackage,
	                                List<ClassName> presenterClassNames,
	                                List<TypeElement> presentersContainers,
	                                List<ClassName> strategyClasses,
	                                List<String> additionalMoxyReflectorsPackages) {
		// sort to preserve order between compilations
		Map<TypeElement, List<TypeElement>> presenterBinders = getPresenterBinders(presentersContainers);
		Collections.sort(presenterClassNames);
		Collections.sort(strategyClasses);
		Collections.sort(additionalMoxyReflectorsPackages);

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder("MoxyReflector")
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
				.addField(MAP_CLASS_TO_OBJECT_TYPE_NAME, "sViewStateProviders", Modifier.PRIVATE, Modifier.STATIC)
				.addField(MAP_CLASS_TO_LIST_OF_OBJECT_TYPE_NAME, "sPresenterBinders", Modifier.PRIVATE, Modifier.STATIC)
				.addField(MAP_CLASS_TO_OBJECT_TYPE_NAME, "sStrategies", Modifier.PRIVATE, Modifier.STATIC);

		classBuilder.addStaticBlock(generateStaticInitializer(presenterClassNames, presenterBinders,
				strategyClasses, additionalMoxyReflectorsPackages));

		if (destinationPackage.equals(MOXY_REFLECTOR_DEFAULT_PACKAGE)) {
			classBuilder.addMethod(MethodSpec.methodBuilder("getViewState")
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
					.returns(Object.class)
					.addParameter(CLASS_WILDCARD_TYPE_NAME, "presenterClass")
					.addStatement("$1T viewStateProvider = ($1T) sViewStateProviders.get(presenterClass)", ViewStateProvider.class)
					.beginControlFlow("if (viewStateProvider == null)")
					.addStatement("return null")
					.endControlFlow()
					.addCode("\n")
					.addStatement("return viewStateProvider.getViewState()")
					.build());

			classBuilder.addMethod(MethodSpec.methodBuilder("getPresenterBinders")
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
					.returns(ParameterizedTypeName.get(List.class, Object.class))
					.addParameter(CLASS_WILDCARD_TYPE_NAME, "delegated")
					.addStatement("return sPresenterBinders.get(delegated)")
					.build());

			classBuilder.addMethod(MethodSpec.methodBuilder("getStrategy")
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
					.returns(Object.class)
					.addParameter(CLASS_WILDCARD_TYPE_NAME, "strategyClass")
					.addStatement("return sStrategies.get(strategyClass)")
					.build());
		} else {
			classBuilder.addMethod(MethodSpec.methodBuilder("getViewStateProviders")
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
					.returns(MAP_CLASS_TO_OBJECT_TYPE_NAME)
					.addStatement("return viewStateProvider.getViewState()")
					.build());

			classBuilder.addMethod(MethodSpec.methodBuilder("getPresenterBinders")
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
					.returns(MAP_CLASS_TO_LIST_OF_OBJECT_TYPE_NAME)
					.addStatement("return sViewStateProviders")
					.build());

			classBuilder.addMethod(MethodSpec.methodBuilder("getStrategies")
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
					.returns(MAP_CLASS_TO_OBJECT_TYPE_NAME)
					.addStatement("return sStrategies")
					.build());
		}


		return JavaFile.builder(destinationPackage, classBuilder.build())
				.indent("\t")
				.build();
	}

	private static CodeBlock generateStaticInitializer(List<ClassName> presenterClassNames, Map<TypeElement, List<TypeElement>> presenterBinders, List<ClassName> strategyClasses, List<String> additionalMoxyReflectorsPackages) {
		CodeBlock.Builder staticBlockBuilder = CodeBlock.builder();

		staticBlockBuilder.addStatement("sViewStateProviders = new $T<>()", HashMap.class);
		for (ClassName presenter : presenterClassNames) {
			ClassName viewStateProvider = presenter.peerClass(presenter.simpleName() + MvpProcessor.VIEW_STATE_PROVIDER_SUFFIX);
			staticBlockBuilder.addStatement("sViewStateProviders.put($T.class, new $T())", presenter, viewStateProvider);
		}

		staticBlockBuilder.add("\n");

		staticBlockBuilder.addStatement("sPresenterBinders = new $T<>()", HashMap.class);
		for (Map.Entry<TypeElement, List<TypeElement>> keyValue : presenterBinders.entrySet()) {
			staticBlockBuilder.add("sPresenterBinders.put($T.class, $T.<Object>asList(", keyValue.getKey(), Arrays.class);

			boolean isFirst = true;
			for (TypeElement typeElement : keyValue.getValue()) {
				ClassName className = ClassName.get(typeElement);
				String presenterBinderName = join("$", className.simpleNames()) + MvpProcessor.PRESENTER_BINDER_SUFFIX;

				if (isFirst) {
					isFirst = false;
				} else {
					staticBlockBuilder.add(", ");
				}
				staticBlockBuilder.add("new $T()", ClassName.get(className.packageName(), presenterBinderName));
			}

			staticBlockBuilder.add("));\n");
		}

		staticBlockBuilder.add("\n");

		staticBlockBuilder.addStatement("sStrategies = new $T<>()", HashMap.class);

		for (ClassName strategyClass : strategyClasses) {
			staticBlockBuilder.addStatement("sStrategies.put($1T.class, new $1T())", strategyClass);
		}

		for (String pkg : additionalMoxyReflectorsPackages) {
			ClassName moxyReflector = ClassName.get(pkg, "MoxyReflector");

			staticBlockBuilder.add("\n");
			staticBlockBuilder.addStatement("sViewStateProviders.putAll($T.getViewStateProviders())", moxyReflector);
			staticBlockBuilder.addStatement("sPresenterBinders.putAll($T.getPresenterBinders())", moxyReflector);
			staticBlockBuilder.addStatement("sStrategies.putAll($T.getStrategies())", moxyReflector);
		}

		return staticBlockBuilder.build();
	}

	/**
	 * Collects presenter binders from superclasses that are also presenter containers.
	 *
	 * @return mapping between presenter container and list of corresponding binders
	 */
	private static Map<TypeElement, List<TypeElement>> getPresenterBinders(List<TypeElement> presentersContainers) {
		Map<TypeElement, TypeElement> extendingMap = new HashMap<>();

		for (TypeElement presentersContainer : presentersContainers) {
			TypeMirror superclass = presentersContainer.getSuperclass();

			TypeElement parent = null;

			while (superclass.getKind() == TypeKind.DECLARED) {
				TypeElement superclassElement = (TypeElement) ((DeclaredType) superclass).asElement();

				if (presentersContainers.contains(superclassElement)) {
					parent = superclassElement;
					break;
				}

				superclass = superclassElement.getSuperclass();
			}

			extendingMap.put(presentersContainer, parent);
		}

		// TreeMap for sorting
		Map<TypeElement, List<TypeElement>> elementListMap = new TreeMap<>(TYPE_ELEMENT_COMPARATOR);

		for (TypeElement presentersContainer : presentersContainers) {
			ArrayList<TypeElement> typeElements = new ArrayList<>();
			typeElements.add(presentersContainer);

			TypeElement key = presentersContainer;
			while ((key = extendingMap.get(key)) != null) {
				typeElements.add(key);
			}

			elementListMap.put(presentersContainer, typeElements);
		}
		return elementListMap;
	}
}
