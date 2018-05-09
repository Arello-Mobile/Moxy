package com.arellomobile.mvp.compiler.viewstateprovider;

import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.ViewStateProvider;
import com.arellomobile.mvp.compiler.JavaFilesGenerator;
import com.arellomobile.mvp.viewstate.MvpViewState;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * Date: 19-Jan-16
 * Time: 19:51
 *
 * @author Alexander Blinov
 */
public final class ViewStateProviderClassGenerator extends JavaFilesGenerator<PresenterInfo> {

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
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getViewState")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(ParameterizedTypeName.get(ClassName.get(MvpViewState.class), WildcardTypeName.subtypeOf(MvpView.class)));

		if (viewState == null) {
			methodBuilder.addStatement("throw new RuntimeException($S)", presenter.reflectionName() + " should has view");
		} else {
			methodBuilder.addStatement("return new $T()", viewState);
		}

		return methodBuilder.build();
	}
}

