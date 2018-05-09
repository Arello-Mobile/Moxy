package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.RegisterMoxyReflectorPackages;
import com.arellomobile.mvp.compiler.presenterbinder.InjectPresenterProcessor;
import com.arellomobile.mvp.compiler.presenterbinder.PresenterBinderClassGenerator;
import com.arellomobile.mvp.compiler.reflector.MoxyReflectorGenerator;
import com.arellomobile.mvp.compiler.viewstate.ViewInterfaceProcessor;
import com.arellomobile.mvp.compiler.viewstate.ViewStateClassGenerator;
import com.arellomobile.mvp.compiler.viewstateprovider.InjectViewStateProcessor;
import com.arellomobile.mvp.compiler.viewstateprovider.ViewStateProviderClassGenerator;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
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
	public static final String MOXY_REFLECTOR_DEFAULT_PACKAGE = "com.arellomobile.mvp";

	private static final String OPTION_MOXY_REFLECTOR_PACKAGE = "moxyReflectorPackage";

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
	public Set<String> getSupportedOptions() {
		return Collections.singleton(OPTION_MOXY_REFLECTOR_PACKAGE);
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
			getMessager().printMessage(Diagnostic.Kind.OTHER, "Moxy compilation failed. Could you copy stack trace above and write us (or make issue on Github)?");
			e.printStackTrace();
			getMessager().printMessage(Diagnostic.Kind.ERROR, "Moxy compilation failed; see the compiler error output for details (" + e + ")");
		}

		return true;
	}

	private boolean throwableProcess(RoundEnvironment roundEnv) {
		checkInjectors(roundEnv, InjectPresenter.class, new PresenterInjectorRules(ElementKind.FIELD, Modifier.PUBLIC, Modifier.DEFAULT));

		InjectViewStateProcessor injectViewStateProcessor = new InjectViewStateProcessor();
		ViewStateProviderClassGenerator viewStateProviderClassGenerator = new ViewStateProviderClassGenerator();

		InjectPresenterProcessor injectPresenterProcessor = new InjectPresenterProcessor();
		PresenterBinderClassGenerator presenterBinderClassGenerator = new PresenterBinderClassGenerator();

		ViewInterfaceProcessor viewInterfaceProcessor = new ViewInterfaceProcessor();
		ViewStateClassGenerator viewStateClassGenerator = new ViewStateClassGenerator();

		processInjectors(roundEnv, InjectViewState.class, ElementKind.CLASS,
				injectViewStateProcessor, viewStateProviderClassGenerator);
		processInjectors(roundEnv, InjectPresenter.class, ElementKind.FIELD,
				injectPresenterProcessor, presenterBinderClassGenerator);

		for (TypeElement usedView : injectViewStateProcessor.getUsedViews()) {
			generateCode(usedView, ElementKind.INTERFACE,
					viewInterfaceProcessor, viewStateClassGenerator);
		}

		String moxyReflectorPackage = sOptions.get(OPTION_MOXY_REFLECTOR_PACKAGE);

		if (moxyReflectorPackage == null) {
			moxyReflectorPackage = MOXY_REFLECTOR_DEFAULT_PACKAGE;
		}

		List<String> additionalMoxyReflectorPackages = getAdditionalMoxyReflectorPackages(roundEnv);

		JavaFile moxyReflector = MoxyReflectorGenerator.generate(
				moxyReflectorPackage,
				injectViewStateProcessor.getPresenterClassNames(),
				injectPresenterProcessor.getPresentersContainers(),
				viewInterfaceProcessor.getUsedStrategies(),
				additionalMoxyReflectorPackages
		);

		createSourceFile(moxyReflector);

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

	private <E extends Element, R> void processInjectors(RoundEnvironment roundEnv,
	                                                     Class<? extends Annotation> clazz,
	                                                     ElementKind kind,
	                                                     ElementProcessor<E, R> processor,
	                                                     JavaFilesGenerator<R> classGenerator) {
		for (Element element : roundEnv.getElementsAnnotatedWith(clazz)) {
			if (element.getKind() != kind) {
				getMessager().printMessage(Diagnostic.Kind.ERROR,
						element + " must be " + kind.name() + ", or not mark it as @" + clazz.getSimpleName());
			}

			generateCode(element, kind, processor, classGenerator);
		}
	}

	private <E extends Element, R> void generateCode(Element element,
	                                                 ElementKind kind,
	                                                 ElementProcessor<E, R> processor,
	                                                 JavaFilesGenerator<R> classGenerator) {
		if (element.getKind() != kind) {
			getMessager().printMessage(Diagnostic.Kind.ERROR, element + " must be " + kind.name());
		}

		//noinspection unchecked
		R result = processor.process((E) element);

		if (result == null) return;

		for (JavaFile file : classGenerator.generate(result)) {
			createSourceFile(file);
		}
	}

	private void createSourceFile(JavaFile file) {
		try {
			file.writeTo(processingEnv.getFiler());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
