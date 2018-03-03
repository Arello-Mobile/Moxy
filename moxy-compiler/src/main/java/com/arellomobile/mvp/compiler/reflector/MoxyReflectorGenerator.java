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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.arellomobile.mvp.compiler.MvpCompiler.MOXY_REFLECTOR_DEFAULT_PACKAGE;

/**
 * Date: 07.12.2016
 * Time: 19:05
 *
 * @author Yuri Shmakov
 */

public class MoxyReflectorGenerator {
	private static final Comparator<TypeElement> TYPE_ELEMENT_COMPARATOR = Comparator.comparing(Object::toString);

	private static final TypeName CLASS_WILDCARD_TYPE_NAME // Class<*>
			= ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(TypeName.OBJECT));
	private static final TypeName LIST_OF_OBJECT_TYPE_NAME // List<Object>
			= ParameterizedTypeName.get(ClassName.get(List.class), TypeName.OBJECT);
	private static final TypeName MAP_CLASS_TO_OBJECT_TYPE_NAME // Map<Class<*>, Object>
			= ParameterizedTypeName.get(ClassName.get(Map.class), CLASS_WILDCARD_TYPE_NAME, TypeName.OBJECT);
	private static final TypeName MAP_CLASS_TO_LIST_OF_OBJECT_TYPE_NAME // Map<Class<*>, List<Object>>
			= ParameterizedTypeName.get(ClassName.get(Map.class), CLASS_WILDCARD_TYPE_NAME, LIST_OF_OBJECT_TYPE_NAME);

	public static JavaFile generate(String destinationPackage,
	                                List<TypeElement> presenterClassNames,
	                                List<TypeElement> presentersContainers,
	                                List<TypeElement> strategyClasses,
	                                List<String> additionalMoxyReflectorsPackages) {
		TypeSpec.Builder classBuilder = TypeSpec.classBuilder("MoxyReflector")
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
				.addField(MAP_CLASS_TO_OBJECT_TYPE_NAME, "sViewStateProviders", Modifier.PRIVATE, Modifier.STATIC)
				.addField(MAP_CLASS_TO_LIST_OF_OBJECT_TYPE_NAME, "sPresenterBinders", Modifier.PRIVATE, Modifier.STATIC)
				.addField(MAP_CLASS_TO_OBJECT_TYPE_NAME, "sStrategies", Modifier.PRIVATE, Modifier.STATIC);

		classBuilder.addStaticBlock(generateStaticInitializer(presenterClassNames, presentersContainers, strategyClasses, additionalMoxyReflectorsPackages));

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

	private static CodeBlock generateStaticInitializer(List<TypeElement> presenterClassNames,
	                                                   List<TypeElement> presentersContainers,
	                                                   List<TypeElement> strategyClasses,
	                                                   List<String> additionalMoxyReflectorsPackages) {
		// sort to preserve order of statements between compilations
		Map<TypeElement, List<TypeElement>> presenterBinders = getPresenterBinders(presentersContainers);
		presenterClassNames.sort(TYPE_ELEMENT_COMPARATOR);
		strategyClasses.sort(TYPE_ELEMENT_COMPARATOR);
		additionalMoxyReflectorsPackages.sort(Comparator.naturalOrder());

		CodeBlock.Builder builder = CodeBlock.builder();

		builder.addStatement("sViewStateProviders = new $T<>()", HashMap.class);
		for (TypeElement presenter : presenterClassNames) {
			ClassName presenterClassName = ClassName.get(presenter);
			ClassName viewStateProvider = ClassName.get(presenterClassName.packageName(),
					String.join("$", presenterClassName.simpleNames()) + MvpProcessor.VIEW_STATE_PROVIDER_SUFFIX);
			builder.addStatement("sViewStateProviders.put($T.class, new $T())", presenterClassName, viewStateProvider);
		}

		builder.add("\n");

		builder.addStatement("sPresenterBinders = new $T<>()", HashMap.class);
		for (Map.Entry<TypeElement, List<TypeElement>> keyValue : presenterBinders.entrySet()) {
			builder.add("sPresenterBinders.put($T.class, $T.<Object>asList(", keyValue.getKey(), Arrays.class);

			boolean isFirst = true;
			for (TypeElement typeElement : keyValue.getValue()) {
				ClassName className = ClassName.get(typeElement);
				String presenterBinderName = String.join("$", className.simpleNames()) + MvpProcessor.PRESENTER_BINDER_SUFFIX;

				if (isFirst) {
					isFirst = false;
				} else {
					builder.add(", ");
				}
				builder.add("new $T()", ClassName.get(className.packageName(), presenterBinderName));
			}

			builder.add("));\n");
		}

		builder.add("\n");

		builder.addStatement("sStrategies = new $T<>()", HashMap.class);
		for (TypeElement strategyClass : strategyClasses) {
			builder.addStatement("sStrategies.put($1T.class, new $1T())", strategyClass);
		}

		for (String pkg : additionalMoxyReflectorsPackages) {
			ClassName moxyReflector = ClassName.get(pkg, "MoxyReflector");

			builder.add("\n");
			builder.addStatement("sViewStateProviders.putAll($T.getViewStateProviders())", moxyReflector);
			builder.addStatement("sPresenterBinders.putAll($T.getPresenterBinders())", moxyReflector);
			builder.addStatement("sStrategies.putAll($T.getStrategies())", moxyReflector);
		}

		return builder.build();
	}

	/**
	 * Collects presenter binders from superclasses that are also presenter containers.
	 *
	 * @return sorted map between presenter container and list of corresponding binders
	 */
	private static SortedMap<TypeElement, List<TypeElement>> getPresenterBinders(List<TypeElement> presentersContainers) {
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
		SortedMap<TypeElement, List<TypeElement>> elementListMap = new TreeMap<>(TYPE_ELEMENT_COMPARATOR);

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
