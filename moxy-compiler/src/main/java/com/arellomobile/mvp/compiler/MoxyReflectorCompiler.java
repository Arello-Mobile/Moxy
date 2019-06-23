package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.RegisterMoxyReflectorPackages;
import com.arellomobile.mvp.compiler.presenterbinder.InjectPresenterProcessor;
import com.arellomobile.mvp.compiler.reflector.MoxyReflectorGenerator;
import com.arellomobile.mvp.compiler.viewstate.ViewInterfaceProcessor;
import com.arellomobile.mvp.compiler.viewstateprovider.InjectViewStateProcessor;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.auto.service.AutoService;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.AGGREGATING)
public class MoxyReflectorCompiler extends MoxyBaseCompiler {

    public static final String MOXY_REFLECTOR_DEFAULT_PACKAGE = "com.arellomobile.mvp";
    private static final String OPTION_MOXY_REFLECTOR_PACKAGE = "moxyReflectorPackage";

    private final Map<String, String> processorOptions = new HashMap<>();

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        processorOptions.putAll(processingEnv.getOptions());
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Collections.singleton(OPTION_MOXY_REFLECTOR_PACKAGE);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> supportedAnnotationTypes = new HashSet<>();
        Collections.addAll(supportedAnnotationTypes,
                InjectPresenter.class.getCanonicalName(),
                InjectViewState.class.getCanonicalName(),
                GenerateViewState.class.getCanonicalName(),
                RegisterMoxyReflectorPackages.class.getCanonicalName());
        return supportedAnnotationTypes;
    }

    @Override
    protected void throwableProcess(RoundEnvironment roundEnv) {
        String moxyReflectorPackage = processorOptions.get(OPTION_MOXY_REFLECTOR_PACKAGE);
        if (moxyReflectorPackage == null) {
            moxyReflectorPackage = MOXY_REFLECTOR_DEFAULT_PACKAGE;
        }

        final InjectViewStateProcessor injectViewStateProcessor = new InjectViewStateProcessor();
        final InjectPresenterProcessor injectPresenterProcessor = new InjectPresenterProcessor();
        final ViewInterfaceProcessor viewInterfaceProcessor = new ViewInterfaceProcessor();
        processInjectors(roundEnv, InjectViewState.class, ElementKind.CLASS, injectViewStateProcessor);
        processInjectors(roundEnv, InjectPresenter.class, ElementKind.FIELD, injectPresenterProcessor);
        final List<String> additionalMoxyReflectorPackages = getAdditionalMoxyReflectorPackages(roundEnv);
        try {
            MoxyReflectorGenerator.generate(
                    moxyReflectorPackage,
                    injectViewStateProcessor.getPresenterClassNames(),
                    injectPresenterProcessor.getPresentersContainers(),
                    viewInterfaceProcessor.getUsedStrategies(),
                    additionalMoxyReflectorPackages
            ).writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <E extends Element, R> void processInjectors(RoundEnvironment roundEnv,
                                                           Class<? extends Annotation> clazz,
                                                           ElementKind kind,
                                                           ElementProcessor<E, R> processor) {

        for (Element element : roundEnv.getElementsAnnotatedWith(clazz)) {
            if (element.getKind() != kind) {
                ProcessingEnvironmentHolder.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        element + " must be " + kind.name() + ", or not mark it as @" + clazz.getSimpleName());
            }

            if (element.getKind() != kind) {
                ProcessingEnvironmentHolder.getMessager().printMessage(Diagnostic.Kind.ERROR, element + " must be " + kind.name());
            }

            //noinspection unchecked
            processor.process((E) element);
        }
    }

    private List<String> getAdditionalMoxyReflectorPackages(RoundEnvironment roundEnv) {
        List<String> result = new ArrayList<>();

        for (Element element : roundEnv.getElementsAnnotatedWith(RegisterMoxyReflectorPackages.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, element + " must be " + ElementKind.CLASS.name() + ", or not mark it as @" + RegisterMoxyReflectorPackages.class.getSimpleName());
            }

            String[] packages = element.getAnnotation(RegisterMoxyReflectorPackages.class).value();

            Collections.addAll(result, packages);
        }

        return result;
    }
}
