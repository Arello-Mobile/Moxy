package com.omegar.mvp.compiler.viewstate;

import com.omegar.mvp.MvpProcessor;
import com.omegar.mvp.compiler.JavaFilesGenerator;
import com.omegar.mvp.compiler.MvpCompiler;
import com.omegar.mvp.viewstate.MvpViewState;
import com.omegar.mvp.viewstate.ViewCommand;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;

import static com.omegar.mvp.compiler.Util.decapitalizeString;

/**
 * Date: 18.12.2015
 * Time: 13:24
 *
 * @author Yuri Shmakov
 */
public final class ViewStateClassGenerator extends JavaFilesGenerator<List<ViewInterfaceInfo>> {

	private static final String T = "T";
	private static final TypeVariableName GENERIC_TYPE_VARIABLE_NAME = TypeVariableName.get(T);
	private static final ClassName MVP_VIEW_STATE_CLASS_NAME = ClassName.get(MvpViewState.class);
	private static final ClassName VIEW_COMMAND_CLASS_NAME = ClassName.get(ViewCommand.class);
	private static final ParameterizedTypeName VIEW_COMMAND_TYPE_NAME
			= ParameterizedTypeName.get(VIEW_COMMAND_CLASS_NAME, GENERIC_TYPE_VARIABLE_NAME);
	private static final ParameterizedTypeName MVP_VIEW_STATE_TYPE_NAME
			= ParameterizedTypeName.get(MVP_VIEW_STATE_CLASS_NAME, GENERIC_TYPE_VARIABLE_NAME);

	@Override
	public List<JavaFile> generate(List<ViewInterfaceInfo> list) {
		if (list.isEmpty()) return Collections.emptyList();

		MvpCompiler.getMessager().printMessage(Diagnostic.Kind.WARNING, "Size " + list.size());

		List<JavaFile> fileList = new ArrayList<>();
		fileList.add(generate(list.get(0)));

		for (int i = 1; i < list.size(); i++) {
			ViewInterfaceInfo info = list.get(i);

			JavaFile parentClassFile = fileList.get(fileList.size() - 1);
			ClassName parentClassName = ClassName.get(parentClassFile.packageName, parentClassFile.typeSpec.name);

			fileList.add(generate(info, parentClassName));
		}
		return fileList;
	}

	private JavaFile generate(ViewInterfaceInfo viewInterfaceInfo) {
		return generate(viewInterfaceInfo, null);
	}

	private JavaFile generate(ViewInterfaceInfo viewInterfaceInfo, @Nullable ClassName parentClassName) {
		ClassName viewName = viewInterfaceInfo.getName();
		TypeName nameWithTypeVariables = viewInterfaceInfo.getNameWithTypeVariables();
		DeclaredType viewInterfaceType = (DeclaredType) viewInterfaceInfo.getElement().asType();
		TypeVariableName variableName = TypeVariableName.get(T, nameWithTypeVariables);

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(viewName.simpleName() + MvpProcessor.VIEW_STATE_SUFFIX)
				.addModifiers(Modifier.PUBLIC)
				.superclass(parentClassName == null ? MVP_VIEW_STATE_TYPE_NAME :
						ParameterizedTypeName.get(parentClassName, variableName))
				.addTypeVariable(variableName)
				.addSuperinterface(nameWithTypeVariables);

		for (ViewMethod method : viewInterfaceInfo.getMethods()) {
			TypeSpec commandClass = generateCommandClass(method, nameWithTypeVariables);
			classBuilder.addType(commandClass);
			classBuilder.addMethod(generateMethod(viewInterfaceType, method, nameWithTypeVariables, commandClass));
		}

		return JavaFile.builder(viewName.packageName(), classBuilder.build())
				.indent("\t")
				.build();
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
				.superclass(VIEW_COMMAND_TYPE_NAME)
				.addMethod(generateCommandConstructor(method))
				.addMethod(applyMethod);

		for (ParameterSpec parameter : method.getParameterSpecs()) {
			// TODO: private field
			classBuilder.addField(parameter.type, parameter.name, Modifier.PUBLIC, Modifier.FINAL);
		}

		return classBuilder.build();
	}

	private MethodSpec generateMethod(DeclaredType enclosingType, ViewMethod method,
	                                  TypeName viewTypeName, TypeSpec commandClass) {
		// TODO: String commandFieldName = "$cmd";
		String commandFieldName = decapitalizeString(method.getCommandClassName());

		// Add salt if contains argument with same name
		Random random = new Random();
		while (method.getArgumentsString().contains(commandFieldName)) {
			commandFieldName += random.nextInt(10);
		}

		return MethodSpec.overriding(method.getElement(), enclosingType, MvpCompiler.getTypeUtils())
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
