package com.arellomobile.mvp.compiler.presenterbinder;

import com.arellomobile.mvp.MvpProcessor;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

class TargetPresenterField {
    private final TypeMirror clazz;

    private final boolean isParametrized;

    private final TypeName typeName;

    private final String name;

    private final String tag;

    private final String presenterId;

    private String presenterProviderMethodName;

    private String presenterTagProviderMethodName;

    TargetPresenterField(TypeMirror clazz,
            String name,
            String tag,
            String presenterId) {
        this.clazz = clazz;
        this.isParametrized = TypeName.get(clazz) instanceof ParameterizedTypeName;
        this.typeName = isParametrized ? ((ParameterizedTypeName) TypeName.get(clazz)).rawType : TypeName.get(clazz);
        this.name = name;
        this.tag = tag;
        this.presenterId = presenterId;
    }

    TypeMirror getClazz() {
        return clazz;
    }

    TypeName getTypeName() {
        return typeName;
    }

    String getGeneratedClassName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1) + MvpProcessor.PRESENTER_BINDER_INNER_SUFFIX;
    }

    String getTag() {
        return tag;
    }

    String getName() {
        return name;
    }

    String getPresenterId() {
        return presenterId;
    }

    String getPresenterProviderMethodName() {
        return presenterProviderMethodName;
    }

    void setPresenterProviderMethodName(String presenterProviderMethodName) {
        this.presenterProviderMethodName = presenterProviderMethodName;
    }

    String getPresenterTagProviderMethodName() {
        return presenterTagProviderMethodName;
    }

    void setPresenterTagProviderMethodName(String presenterTagProviderMethodName) {
        this.presenterTagProviderMethodName = presenterTagProviderMethodName;
    }
}
