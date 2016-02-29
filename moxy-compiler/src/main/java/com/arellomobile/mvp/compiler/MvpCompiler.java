package com.arellomobile.mvp.compiler;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.ParamsProvider;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.auto.service.AutoService;

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
import javax.tools.JavaFileObject;


import static javax.lang.model.SourceVersion.latestSupported;

@SuppressWarnings("unused")
@AutoService(Processor.class)
public class MvpCompiler extends AbstractProcessor
{
	private static Messager sMessager;
	private static Types sTypeUtils;
	private static Elements sElementUtils;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv)
	{
		super.init(processingEnv);

		sMessager = processingEnv.getMessager();
		sTypeUtils = processingEnv.getTypeUtils();
		sElementUtils = processingEnv.getElementUtils();
	}

	public static Messager getMessager()
	{
		return sMessager;
	}

	public static Types getTypeUtils()
	{
		return sTypeUtils;
	}

	public static Elements getElementUtils()
	{
		return sElementUtils;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes()
	{
		Set<String> supportedAnnotationTypes = new HashSet<>();
		Collections.addAll(supportedAnnotationTypes, InjectPresenter.class.getCanonicalName(), ParamsProvider.class.getCanonicalName(), InjectViewState.class.getCanonicalName(), GenerateViewState.class.getCanonicalName());
		return supportedAnnotationTypes;
	}

	@Override
	public SourceVersion getSupportedSourceVersion()
	{
		return latestSupported();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
	{
		checkInjectors(roundEnv, InjectPresenter.class, new PresenterInjectorRules(ElementKind.FIELD, Modifier.PUBLIC, Modifier.DEFAULT));

		processInjectors(roundEnv, InjectViewState.class, ElementKind.CLASS, new ViewStateProviderClassGenerator());
		processInjectors(roundEnv, InjectPresenter.class, ElementKind.FIELD, new PresenterBinderClassGenerator());
		processInjectors(roundEnv, ParamsProvider.class, ElementKind.INTERFACE, new ParamsHolderClassGenerator());
		processInjectors(roundEnv, GenerateViewState.class, ElementKind.INTERFACE, new ViewStateClassGenerator());

		return true;
	}


	private void checkInjectors(final RoundEnvironment roundEnv, Class<? extends Annotation> clazz, AnnotationRule annotationRule)
	{
		for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(clazz))
		{
			annotationRule.checkAnnotation(annotatedElement);
		}

		if (!annotationRule.getErrorStack().isEmpty())
		{
			throw new RuntimeException("\n" + annotationRule.getErrorStack());
		}
	}

	private void processInjectors(final RoundEnvironment roundEnv, Class<? extends Annotation> clazz, ElementKind kind, ClassGenerator classGenerator)
	{
		for (Element annotatedElements : roundEnv.getElementsAnnotatedWith(clazz))
		{
			if (annotatedElements.getKind() != kind)
			{
				throw new RuntimeException(annotatedElements + " must be " + kind.name() + ", or not mark it as @" + clazz.getSimpleName());
			}

			List<ClassGeneratingParams> classGeneratingParamsList = new ArrayList<>();

			//noinspection unchecked
			final boolean generated = classGenerator.generate(annotatedElements, classGeneratingParamsList);

			if (!generated)
			{
				continue;
			}

			for (ClassGeneratingParams classGeneratingParams : classGeneratingParamsList)
			{
				createSourceFile(classGeneratingParams);
			}
		}
	}

	private void createSourceFile(ClassGeneratingParams classGeneratingParams)
	{
		try
		{
			JavaFileObject f = processingEnv.getFiler().createSourceFile(classGeneratingParams.getName());

			Writer w = f.openWriter();
			w.write(classGeneratingParams.getBody());
			w.flush();
			w.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
