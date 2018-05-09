package com.arellomobile.mvp.compiler.viewstate;

import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.compiler.JavaFilesGenerator;
import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.lang.model.element.Modifier;

import static com.arellomobile.mvp.compiler.Util.decapitalizeString;

/**
 * Date: 18.12.2015
 * Time: 13:24
 *
 * @author Yuri Shmakov
 */
public final class ViewStateClassGenerator extends JavaFilesGenerator<ViewInterfaceInfo> {

	@Override
	public List<JavaFile> generate(ViewInterfaceInfo viewInterfaceInfo) {
		ClassName viewName = viewInterfaceInfo.getName();
		TypeName nameWithTypeVariables = viewInterfaceInfo.getNameWithTypeVariables();

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(viewName.simpleName() + MvpProcessor.VIEW_STATE_SUFFIX)
				.addModifiers(Modifier.PUBLIC)
				.superclass(ParameterizedTypeName.get(ClassName.get(MvpViewState.class), nameWithTypeVariables))
				.addSuperinterface(nameWithTypeVariables)
				.addTypeVariables(viewInterfaceInfo.getTypeVariables());

		for (ViewMethod method : viewInterfaceInfo.getMethods()) {
			TypeSpec commandClass = generateCommandClass(method, nameWithTypeVariables);
			classBuilder.addType(commandClass);
			classBuilder.addMethod(generateMethod(method, nameWithTypeVariables, commandClass));
		}

		JavaFile javaFile = JavaFile.builder(viewName.packageName(), classBuilder.build())
				.indent("\t")
				.build();
		return Collections.singletonList(javaFile);
	}

	private TypeSpec generateCommandClass(ViewMethod method, TypeName viewTypeName) {
		MethodSpec applyMethod = MethodSpec.methodBuilder("apply")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(viewTypeName, "mvpView")
				.addExceptions(method.getExceptions())
				.addStatement("mvpView.$L($L)", method.getName(), method.getArgumentsString())
				.build();

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(method.getCommandClassName())
				.addModifiers(Modifier.PUBLIC) // TODO: private and static
				.addTypeVariables(method.getTypeVariables())
				.superclass(ParameterizedTypeName.get(ClassName.get(ViewCommand.class), viewTypeName))
				.addMethod(generateCommandConstructor(method))
				.addMethod(applyMethod);

		for (ParameterSpec parameter : method.getParameterSpecs()) {
			// TODO: private field
			classBuilder.addField(parameter.type, parameter.name, Modifier.PUBLIC, Modifier.FINAL);
		}

		return classBuilder.build();
	}

	private MethodSpec generateMethod(ViewMethod method, TypeName viewTypeName, TypeSpec commandClass) {
		// TODO: String commandFieldName = "$cmd";
		String commandFieldName = decapitalizeString(method.getCommandClassName());

		// Add salt if contains argument with same name
		Random random = new Random();
		while (method.getArgumentsString().contains(commandFieldName)) {
			commandFieldName += random.nextInt(10);
		}

		return MethodSpec.overriding(method.getElement())
				.addStatement("$1N $2L = new $1N($3L)", commandClass, commandFieldName, method.getArgumentsString())
				.addStatement("mViewCommands.beforeApply($L)", commandFieldName)
				.addCode("\n")
				.beginControlFlow("if (mViews == null || mViews.isEmpty())")
				.addStatement("return")
				.endControlFlow()
				.addCode("\n")
				.beginControlFlow("for ($T view : mViews)", viewTypeName)
				.addStatement("view.$L($L)", method.getName(), method.getArgumentsString())
				.endControlFlow()
				.addCode("\n")
				.addStatement("mViewCommands.afterApply($L)", commandFieldName)
				.build();
	}

	private MethodSpec generateCommandConstructor(ViewMethod method) {
		List<ParameterSpec> parameters = method.getParameterSpecs();

		MethodSpec.Builder builder = MethodSpec.constructorBuilder()
				.addParameters(parameters)
				.addStatement("super($S, $T.class)", method.getTag(), method.getStrategy());

		if (parameters.size() > 0) {
			builder.addCode("\n");
		}

		for (ParameterSpec parameter : parameters) {
			builder.addStatement("this.$1N = $1N", parameter);
		}

		return builder.build();
	}

}
