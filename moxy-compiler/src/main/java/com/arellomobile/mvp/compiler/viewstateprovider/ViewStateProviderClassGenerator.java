package com.arellomobile.mvp.compiler.viewstateprovider;

import com.arellomobile.mvp.DefaultView;
import com.arellomobile.mvp.DefaultViewState;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.ViewStateProvider;
import com.arellomobile.mvp.compiler.ElementProcessor;
import com.arellomobile.mvp.compiler.FileGenerator;
import com.arellomobile.mvp.compiler.MvpCompiler;
import com.arellomobile.mvp.compiler.Util;
import com.arellomobile.mvp.viewstate.MvpViewState;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.arellomobile.mvp.compiler.Util.fillGenerics;

/**
 * Date: 19-Jan-16
 * Time: 19:51
 *
 * @author Alexander Blinov
 */
public final class ViewStateProviderClassGenerator extends FileGenerator<PresenterInfo> {

	@Override
	public List<JavaFile> generate(PresenterInfo presenterInfo) {
		TypeSpec typeSpec = TypeSpec.classBuilder(presenterInfo.getName().simpleName() + MvpProcessor.VIEW_STATE_PROVIDER_SUFFIX)
				.addModifiers(Modifier.PUBLIC)
				.superclass(ViewStateProvider.class)
				.addMethod(generateGetViewStateMethod(presenterInfo.getName(), presenterInfo.getViewStateName()))
				.build();

		JavaFile javaFile = JavaFile.builder(presenterInfo.getName().packageName(), typeSpec)
				.indent("\t")
				.build();

		return Collections.singletonList(javaFile);
	}


	private MethodSpec generateGetViewStateMethod(ClassName presenter, ClassName viewState) {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getViewState");
		methodBuilder.addModifiers(Modifier.PUBLIC);
		methodBuilder.returns(ParameterizedTypeName.get(ClassName.get(MvpViewState.class), WildcardTypeName.subtypeOf(MvpView.class)));

		if (viewState == null) {
			methodBuilder.addStatement("throw new RuntimeException($S)", presenter.reflectionName() + " should has view");
		} else {
			methodBuilder.addStatement("return new $T()", viewState);
		}

		return methodBuilder.build();
	}
}

