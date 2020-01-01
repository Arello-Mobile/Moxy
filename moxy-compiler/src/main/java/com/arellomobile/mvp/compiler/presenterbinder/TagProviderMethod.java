package com.arellomobile.mvp.compiler.presenterbinder;

import javax.lang.model.type.TypeMirror;

class TagProviderMethod {
    private final TypeMirror presenterClass;

    private final String methodName;

    private final String presenterId;

    TagProviderMethod(TypeMirror presenterClass, String methodName, String type, String presenterId) {
        this.presenterClass = presenterClass;
        this.methodName = methodName;
        this.presenterId = presenterId;
    }

    TypeMirror getPresenterClass() {
        return presenterClass;
    }

    String getMethodName() {
        return methodName;
    }

    String getPresenterId() {
        return presenterId;
    }
}
