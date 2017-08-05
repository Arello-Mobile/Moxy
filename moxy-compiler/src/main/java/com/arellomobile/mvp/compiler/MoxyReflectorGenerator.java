package com.arellomobile.mvp.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.arellomobile.mvp.MvpProcessor;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Date: 07.12.2016
 * Time: 19:05
 *
 * @author Yuri Shmakov
 */

public class MoxyReflectorGenerator {

	public static String generate(String moxyReflectorPackage,
	                              List<String> presenterClassNames,
	                              Set<TypeElement> presentersContainers,
	                              Set<String> strategyClasses,
	                              List<String> additionalMoxyReflectorsPackages) {
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

		Map<TypeElement, List<TypeElement>> elementListMap = new HashMap<>();

		for (TypeElement presentersContainer : presentersContainers) {
			ArrayList<TypeElement> typeElements = new ArrayList<>();
			typeElements.add(presentersContainer);

			elementListMap.put(presentersContainer, typeElements);

			TypeElement key = presentersContainer;
			while ((key = extendingMap.get(key)) != null) {
				typeElements.add(key);
			}
		}

		String builder = "package " + moxyReflectorPackage + ";\n" +
		                 "\n" +
		                 "import java.util.Arrays;\n" +
		                 "import java.util.HashMap;\n" +
		                 "import java.util.List;\n" +
		                 "import java.util.Map;\n" +
		                 "\n" +
		                 "public class MoxyReflector {\n\n" +
		                 "\tprivate static Map<Class<?>, Object> sViewStateProviders;\n" +
		                 "\tprivate static Map<Class<?>, List<Object>> sPresenterBinders;\n" +
		                 "\tprivate static Map<Class<?>, Object> sStrategies;\n" +
		                 "\n" +
		                 "\tstatic {\n" +
		                 "\t\tsViewStateProviders = new HashMap<>();\n";

		Collections.sort(presenterClassNames);

		for (String presenterClassName : presenterClassNames) {
			  builder += "\t\tsViewStateProviders.put(" + presenterClassName + ".class, new " + presenterClassName + MvpProcessor.VIEW_STATE_PROVIDER_SUFFIX + "());\n";
		}

		      builder += "\t\t\n"+
		                 "\t\tsPresenterBinders = new HashMap<>();\n";

		List<Map.Entry<TypeElement, List<TypeElement>>> elementListMapEntries = new ArrayList<>(elementListMap.entrySet());
		Collections.sort(elementListMapEntries, new Comparator<Map.Entry<TypeElement, List<TypeElement>>>() {
			@Override
			public int compare(Map.Entry<TypeElement, List<TypeElement>> entry1, Map.Entry<TypeElement, List<TypeElement>> entry2) {
				return entry1.getKey().getQualifiedName().toString().compareTo(entry2.getKey().getQualifiedName().toString());
			}
		});

		for (Map.Entry<TypeElement, List<TypeElement>> keyValue : elementListMapEntries) {
			  builder += "\t\tsPresenterBinders.put(" + keyValue.getKey().getQualifiedName() + ".class, Arrays.<Object>asList(";

			boolean isFirst = true;
			for (TypeElement typeElement : keyValue.getValue()) {
				if (isFirst) {
					isFirst = false;
				} else {
					builder += ", ";
				}
				builder += "new " + Util.getFullClassName(typeElement) + MvpProcessor.PRESENTER_BINDER_SUFFIX + "()";
			}

			  builder += "));\n";
		}

		      builder += "\t\t\n" +
				         "\t\tsStrategies = new HashMap<>();\n";

		List<String> strategyClassesList = new ArrayList<>(strategyClasses);
		Collections.sort(strategyClassesList);

		for (String strategyClass : strategyClassesList) {
			  builder += "\t\tsStrategies.put(" + strategyClass + ", new " + strategyClass.substring(0, strategyClass.lastIndexOf('.')) + "());\n";
		}

		Collections.sort(additionalMoxyReflectorsPackages);

		for (String pkg : additionalMoxyReflectorsPackages) {
		      builder += "\t\t\n";
		      builder += "\t\tsViewStateProviders.putAll(" + pkg + ".MoxyReflector.getViewStateProviders());\n";
		      builder += "\t\tsPresenterBinders.putAll(" + pkg + ".MoxyReflector.getPresenterBinders());\n";
		      builder += "\t\tsStrategies.putAll(" + pkg + ".MoxyReflector.getStrategies());\n";
		}

		      builder += "\t}\n";

		if (moxyReflectorPackage.equals("com.arellomobile.mvp")) {
              builder += "\t\n" +
		                 "\tpublic static Object getViewState(Class<?> presenterClass) {\n" +
		                 "\t\tViewStateProvider viewStateProvider = (ViewStateProvider) sViewStateProviders.get(presenterClass);\n" +
		                 "\t\tif (viewStateProvider == null) {\n" +
		                 "\t\t\treturn null;\n" +
		                 "\t\t}\n" +
		                 "\t\t\n" +
		                 "\t\treturn viewStateProvider.getViewState();\n" +
		                 "\t}\n" +
		                 "\n" +
		                 "\tpublic static List<Object> getPresenterBinders(Class<?> delegated) {\n" +
		                 "\t\treturn sPresenterBinders.get(delegated);\n" +
		                 "\t}\n" +
		                 "\n" +
		                 "\tpublic static Object getStrategy(Class<?> strategyClass) {\n" +
		                 "\t\treturn sStrategies.get(strategyClass);\n" +
		                 "\t}\n" +
		                 "}\n";
		} else {
		      builder += "\t\n" +
					     "\tpublic static Map<Class<?>, Object> getViewStateProviders() {\n" +
					     "\t\treturn sViewStateProviders;\n" +
					     "\t}\n" +
				         "\n" +
					     "\tpublic static Map<Class<?>, List<Object>> getPresenterBinders() {\n" +
					     "\t\treturn sPresenterBinders;\n" +
					     "\t}\n" +
				         "\n" +
					     "\tpublic static Map<Class<?>, Object> getStrategies() {\n" +
					     "\t\treturn sStrategies;\n" +
					     "\t}\n" +
				         "}\n";
		}
		return builder;
	}
}
