package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.ViewStateClassNameProvider;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Date: 18.12.2015
 * Time: 12:45
 *
 * @author Yuri Shmakov
 */
final class PresenterInjectorClassGenerator
{
	public static TypeSpec generate(String presenterClassFullName, String viewInterfaceFullName)
	{
		final String presenterClassName = presenterClassFullName.substring(presenterClassFullName.lastIndexOf(".") + 1);

		final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(presenterClassName + "$$ViewStateClassNameProvider")
				.addModifiers(Modifier.PUBLIC)
				.addSuperinterface(ViewStateClassNameProvider.class)
				.addMethod(MethodSpec.methodBuilder("getViewStateClassName")
						.addModifiers(Modifier.PUBLIC)
						.addStatement("return \"" + viewInterfaceFullName + "$$$$State\"")
						.returns(String.class)
						.build());

		return classBuilder.build();
	}
}
