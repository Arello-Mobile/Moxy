package com.arellomobile.mvp.compiler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes(value = {"*"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public final class ErrorProcessor extends AbstractProcessor {
	Messager messager;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.messager = processingEnv.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getRootElements()) {
			if (element.getSimpleName().toString().equals("InjectPresenterTypeBehaviorView")) {
				for (Element element1 : element.getEnclosedElements()) {
					System.out.println("EnclosedElements: " + element1.getSimpleName());
					ImmutableList<String> of = ImmutableList.of("mPresenterIdLocalPresenter", "mTagLocalPresenter", "mFactoryLocalPresenter", "mFactoryTagPresenter");
					if (of.contains(element1.getSimpleName().toString())) {
						messager.printMessage(Diagnostic.Kind.ERROR, "expected error!", element1);
					}
				}
			}
		}
		return true;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return ImmutableSet.of("*");
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
}