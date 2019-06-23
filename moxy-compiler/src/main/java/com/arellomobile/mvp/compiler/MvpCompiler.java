package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.compiler.presenterbinder.InjectPresenterProcessor;
import com.arellomobile.mvp.compiler.presenterbinder.PresenterBinderClassGenerator;
import com.arellomobile.mvp.compiler.viewstate.ViewInterfaceProcessor;
import com.arellomobile.mvp.compiler.viewstate.ViewStateClassGenerator;
import com.arellomobile.mvp.compiler.viewstateprovider.InjectViewStateProcessor;
import com.arellomobile.mvp.compiler.viewstateprovider.ViewStateProviderClassGenerator;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 12.12.2015
 * Time: 15:35
 *
 * @author Yuri Shmakov
 */

@SuppressWarnings("unused")
@AutoService(Processor.class)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.ISOLATING)
public class MvpCompiler extends MoxyBaseCompiler {

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> supportedAnnotationTypes = new HashSet<>();
		Collections.addAll(supportedAnnotationTypes,
				InjectPresenter.class.getCanonicalName(),
				InjectViewState.class.getCanonicalName(),
				GenerateViewState.class.getCanonicalName());
		return supportedAnnotationTypes;
	}

	@Override
	protected void throwableProcess(RoundEnvironment roundEnv) {
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
			generateCode(usedView, ElementKind.INTERFACE, viewInterfaceProcessor, viewStateClassGenerator);
		}
	}

	private void checkInjectors(final RoundEnvironment roundEnv, Class<? extends Annotation> clazz, AnnotationRule annotationRule) {
		for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(clazz)) {
			annotationRule.checkAnnotation(annotatedElement);
		}

		String errorStack = annotationRule.getErrorStack();
		if (errorStack != null && errorStack.length() > 0) {
			processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, errorStack);
		}
	}

	private <E extends Element, R> void processInjectors(RoundEnvironment roundEnv,
	                                                     Class<? extends Annotation> clazz,
	                                                     ElementKind kind,
	                                                     ElementProcessor<E, R> processor,
	                                                     JavaFilesGenerator<R> classGenerator) {
		for (Element element : roundEnv.getElementsAnnotatedWith(clazz)) {
			if (element.getKind() != kind) {
				processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
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
			processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, element + " must be " + kind.name());
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
