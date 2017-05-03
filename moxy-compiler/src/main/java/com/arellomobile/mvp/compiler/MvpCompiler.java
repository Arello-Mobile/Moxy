package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.RegisterMoxyReflectorPackages;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import static javax.lang.model.SourceVersion.latestSupported;

/**
 * Date: 12.12.2015
 * Time: 15:35
 *
 * @author Yuri Shmakov
 */

@SuppressWarnings("unused")
@AutoService(Processor.class)
public class MvpCompiler extends AbstractProcessor {
	private static Messager sMessager;
	private static Types sTypeUtils;
	private static Elements sElementUtils;
	private static Map<String, String> sOptions;

	public static Messager getMessager() {
		return sMessager;
	}

	public static Types getTypeUtils() {
		return sTypeUtils;
	}

	public static Elements getElementUtils() {
		return sElementUtils;
	}

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);

		sMessager = processingEnv.getMessager();
		sTypeUtils = processingEnv.getTypeUtils();
		sElementUtils = processingEnv.getElementUtils();
		sOptions = processingEnv.getOptions();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> supportedAnnotationTypes = new HashSet<>();
		Collections.addAll(supportedAnnotationTypes,
				InjectPresenter.class.getCanonicalName(),
				InjectViewState.class.getCanonicalName(),
				RegisterMoxyReflectorPackages.class.getCanonicalName(),
				GenerateViewState.class.getCanonicalName());
		return supportedAnnotationTypes;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return latestSupported();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (annotations.isEmpty()) {
			return false;
		}

		try {
			return throwableProcess(roundEnv);
		} catch (RuntimeException e) {
			getMessager().printMessage(Diagnostic.Kind.OTHER, "Moxy compilation failed. Could you copy stack trace above and write us (or make issue on Githhub)?");
			e.printStackTrace();
			getMessager().printMessage(Diagnostic.Kind.ERROR, "Moxy compilation failed; see the compiler error output for details (" + e + ")");
		}

		return true;
	}

	private boolean throwableProcess(RoundEnvironment roundEnv) {
		checkInjectors(roundEnv, InjectPresenter.class, new PresenterInjectorRules(ElementKind.FIELD, Modifier.PUBLIC, Modifier.DEFAULT));

		ViewStateProviderClassGenerator viewStateProviderClassGenerator = new ViewStateProviderClassGenerator();
		PresenterBinderClassGenerator presenterBinderClassGenerator = new PresenterBinderClassGenerator();
		processInjectors(roundEnv, InjectViewState.class, ElementKind.CLASS, viewStateProviderClassGenerator);
		processInjectors(roundEnv, InjectPresenter.class, ElementKind.FIELD, presenterBinderClassGenerator);

		ViewStateClassGenerator viewStateClassGenerator = new ViewStateClassGenerator();
		Set<TypeElement> usedViews = viewStateProviderClassGenerator.getUsedViews();

		for (TypeElement usedView : usedViews) {
			generateCode(ElementKind.INTERFACE, viewStateClassGenerator, usedView);
		}

		String moxyReflectorPackage = sOptions.get("moxyReflectorPackage");

		if (moxyReflectorPackage == null || "".equals(moxyReflectorPackage)) {
			List<String> packages = getAdditionalMoxyReflectorPackages(roundEnv);

			String moxyReflector = MoxyReflectorGenerator.generate(
					viewStateProviderClassGenerator.getPresenterClassNames(),
					presenterBinderClassGenerator.getPresentersContainers(),
					viewStateClassGenerator.getStrategyClasses(),
					packages);

			ClassGeneratingParams classGeneratingParams = new ClassGeneratingParams();
			classGeneratingParams.setName("com.arellomobile.mvp.MoxyReflector");
			classGeneratingParams.setBody(moxyReflector);

			createSourceFile(classGeneratingParams);
		} else {
			String moxyReflector = AdditionalMoxyReflectorGenerator.generate(
					moxyReflectorPackage,
					viewStateProviderClassGenerator.getPresenterClassNames(),
					presenterBinderClassGenerator.getPresentersContainers(),
					viewStateClassGenerator.getStrategyClasses());

			ClassGeneratingParams classGeneratingParams = new ClassGeneratingParams();
			classGeneratingParams.setName(moxyReflectorPackage + ".MoxyReflector");
			classGeneratingParams.setBody(moxyReflector);

			createSourceFile(classGeneratingParams);
		}

		return true;
	}

	private List<String> getAdditionalMoxyReflectorPackages(RoundEnvironment roundEnv) {
		List<String> result = new ArrayList<>();

		for (Element element : roundEnv.getElementsAnnotatedWith(RegisterMoxyReflectorPackages.class)) {
			if (element.getKind() != ElementKind.CLASS) {
				getMessager().printMessage(Diagnostic.Kind.ERROR, element + " must be " + ElementKind.CLASS.name() + ", or not mark it as @" + RegisterMoxyReflectorPackages.class.getSimpleName());
			}

			String[] packages = element.getAnnotation(RegisterMoxyReflectorPackages.class).value();

			Collections.addAll(result, packages);
		}

		return result;
	}


	private void checkInjectors(final RoundEnvironment roundEnv, Class<? extends Annotation> clazz, AnnotationRule annotationRule) {
		for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(clazz)) {
			annotationRule.checkAnnotation(annotatedElement);
		}

		String errorStack = annotationRule.getErrorStack();
		if (errorStack != null && errorStack.length() > 0) {
			getMessager().printMessage(Diagnostic.Kind.ERROR, errorStack);
		}
	}

	private void processInjectors(final RoundEnvironment roundEnv, Class<? extends Annotation> clazz, ElementKind kind, ClassGenerator classGenerator) {
		for (Element annotatedElements : roundEnv.getElementsAnnotatedWith(clazz)) {
			if (annotatedElements.getKind() != kind) {
				getMessager().printMessage(Diagnostic.Kind.ERROR, annotatedElements + " must be " + kind.name() + ", or not mark it as @" + clazz.getSimpleName());
			}

			generateCode(kind, classGenerator, annotatedElements);
		}
	}

	private void generateCode(ElementKind kind, ClassGenerator classGenerator, Element element) {
		if (element.getKind() != kind) {
			getMessager().printMessage(Diagnostic.Kind.ERROR, element + " must be " + kind.name());
		}

		List<ClassGeneratingParams> classGeneratingParamsList = new ArrayList<>();

		//noinspection unchecked
		final boolean generated = classGenerator.generate(element, classGeneratingParamsList);

		if (!generated) {
			return;
		}

		for (ClassGeneratingParams classGeneratingParams : classGeneratingParamsList) {
			createSourceFile(classGeneratingParams);
		}
	}

	private void createSourceFile(ClassGeneratingParams classGeneratingParams) {
		try {
			JavaFileObject f = processingEnv.getFiler().createSourceFile(classGeneratingParams.getName());

			Writer w = f.openWriter();
			w.write(classGeneratingParams.getBody());
			w.flush();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
