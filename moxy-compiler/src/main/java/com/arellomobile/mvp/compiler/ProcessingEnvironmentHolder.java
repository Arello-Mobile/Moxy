package com.arellomobile.mvp.compiler;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class ProcessingEnvironmentHolder {

    private static final Object sAccessLock = new Object();

    private static Messager sMessager;
    private static Types sTypeUtils;
    private static Elements sElementUtils;

    public static void initialize(ProcessingEnvironment environment) {
        synchronized (sAccessLock) {
            sMessager = environment.getMessager();
            sTypeUtils = environment.getTypeUtils();
            sElementUtils = environment.getElementUtils();
        }
    }

    public static Messager getMessager() {
        synchronized (sAccessLock) {
            return sMessager;
        }
    }

    public static Types getTypeUtils() {
        synchronized (sAccessLock) {
            return sTypeUtils;
        }
    }

    public static Elements getElementUtils() {
        synchronized (sAccessLock) {
            return sElementUtils;
        }
    }
}
