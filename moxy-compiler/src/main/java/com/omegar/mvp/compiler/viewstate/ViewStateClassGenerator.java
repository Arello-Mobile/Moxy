package com.omegar.mvp.compiler.viewstate;

import com.omegar.mvp.MvpProcessor;
import com.omegar.mvp.compiler.JavaFilesGenerator;
import com.omegar.mvp.compiler.MvpCompiler;
import com.omegar.mvp.compiler.Util;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import static com.omegar.mvp.compiler.Util.decapitalizeString;

/**
 * Date: 18.12.2015
 * Time: 13:24
 *
 * @author Yuri Shmakov
 */
public final class ViewStateClassGenerator extends JavaFilesGenerator<List<ViewInterfaceInfo>> {

	private static final String VIEW = "Omega$$View";
	private static final TypeVariableName GENERIC_TYPE_VARIABLE_NAME = TypeVariableName.get(VIEW);
	private static final ClassName MVP_VIEW_STATE_CLASS_NAME = ClassName.get(MvpViewState.class);
	private static final ClassName VIEW_COMMAND_CLASS_NAME = ClassName.get(ViewCommand.class);
	private static final ParameterizedTypeName VIEW_COMMAND_TYPE_NAME
			= ParameterizedTypeName.get(VIEW_COMMAND_CLASS_NAME, GENERIC_TYPE_VARIABLE_NAME);
	private static final ParameterizedTypeName MVP_VIEW_STATE_TYPE_NAME
			= ParameterizedTypeName.get(MVP_VIEW_STATE_CLASS_NAME, GENERIC_TYPE_VARIABLE_NAME);

	private Map<ViewInterfaceInfo, JavaFile> filesMap = new HashMap<>();

	@Override
	public List<JavaFile> generate(List<ViewInterfaceInfo> list) {
		if (list.isEmpty()) return Collections.emptyList();
		List<JavaFile> fileList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ViewInterfaceInfo info = list.get(i);

			JavaFile javaFile = filesMap.get(info);
			if (javaFile == null) {
				javaFile = generate(info);
				fileList.add(javaFile);
				filesMap.put(info, javaFile);
			}
			fileList.add(javaFile);
		}
		return fileList;
	}

	private JavaFile generate(ViewInterfaceInfo viewInterfaceInfo) {
		ClassName viewName = viewInterfaceInfo.getName();
		TypeName nameWithTypeVariables = viewInterfaceInfo.getNameWithTypeVariables();
		DeclaredType viewInterfaceType = (DeclaredType) viewInterfaceInfo.getElement().asType();
		TypeVariableName variableName = TypeVariableName.get(VIEW, nameWithTypeVariables);

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(Util.getSimpleClassName(viewInterfaceInfo.getElement()) + MvpProcessor.VIEW_STATE_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(nameWithTypeVariables)
                .addTypeVariables(new ArrayList<TypeVariableName>(viewInterfaceInfo.getTypeVariables()) {{
                	add(0, variableName);
                }});

		ViewInterfaceInfo info = viewInterfaceInfo.getSuperInterfaceInfo();
		JavaFile file = filesMap.get(info);
		if (file == null) {
			classBuilder.superclass(MVP_VIEW_STATE_TYPE_NAME);
		} else {
			ClassName superClassName = ClassName.get(file.packageName, file.typeSpec.name);
			classBuilder.superclass(
					ParameterizedTypeName.get(superClassName, generateSuperClassTypeVariables(viewInterfaceInfo, variableName))
			);
		}

		for (ViewMethod method : viewInterfaceInfo.getMethods()) {
			TypeSpec commandClass = generateCommandClass(method);
			classBuilder.addType(commandClass);
			classBuilder.addMethod(generateMethod(viewInterfaceType, method, nameWithTypeVariables, commandClass));
		}

		return JavaFile.builder(viewName.packageName(), classBuilder.build())
				.indent("\t")
				.build();
	}

	private TypeVariableName[] generateSuperClassTypeVariables(ViewInterfaceInfo viewInterfaceInfo, TypeVariableName variableName) {
		List<TypeVariableName> parentClassTypeVariables = new ArrayList<>();
		parentClassTypeVariables.add(variableName);

		TypeMirror mirror = Util.firstOrNull(viewInterfaceInfo.getElement().getInterfaces());
		if (mirror != null) {
			List<? extends TypeMirror> typeArguments = ((DeclaredType) mirror).getTypeArguments();
			for (TypeMirror typeMirror : typeArguments) {
				TypeName typeName = ClassName.get(typeMirror);
				TypeVariableName name = TypeVariableName.get(typeMirror.toString(), typeName);
				parentClassTypeVariables.add(name);
			}
		}
		//noinspection ToArrayCallWithZeroLengthArrayArgument
		return parentClassTypeVariables.toArray(new TypeVariableName[parentClassTypeVariables.size()]);
	}

	private TypeSpec generateCommandClass(ViewMethod method) {
		MethodSpec applyMethod = MethodSpec.methodBuilder("apply")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(GENERIC_TYPE_VARIABLE_NAME, "mvpView")
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
				.beginControlFlow("for ($T view$$ : mViews)", viewTypeName)
				.addStatement("view$$.$L($L)", method.getName(), method.getArgumentsString())
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
