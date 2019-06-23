package com.arellomobile.mvp.compiler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

import static javax.lang.model.SourceVersion.latestSupported;

public abstract class MoxyBaseCompiler extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        ProcessingEnvironmentHolder.initialize(processingEnv);
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
            throwableProcess(roundEnv);
        } catch (RuntimeException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.OTHER, "Moxy compilation failed. Could you copy stack trace above and write us (or make issue on Github)?");
            e.printStackTrace();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Moxy compilation failed; see the compiler error output for details (" + e + ")");
        }
        return false;
    }

    protected abstract void throwableProcess(RoundEnvironment roundEnv);
}
