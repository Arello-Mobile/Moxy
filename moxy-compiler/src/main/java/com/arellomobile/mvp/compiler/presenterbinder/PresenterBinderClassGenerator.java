package com.arellomobile.mvp.compiler.presenterbinder;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.PresenterBinder;
import com.arellomobile.mvp.compiler.JavaFilesGenerator;
import com.arellomobile.mvp.compiler.Util;
import com.arellomobile.mvp.presenter.PresenterField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

/**
 * 18.12.2015
 * <p>
 * Generates PresenterBinder for class annotated with &#64;InjectPresenters
 * <p>
 * for Sample class with single injected presenter
 * <pre>
 * {@code
 *
 * &#64;InjectPresenters
 * public class Sample extends MvpActivity implements MyView
 * {
 *
 * &#64;InjectPresenter(type = PresenterType.LOCAL, tag = "SOME_TAG")
 * com.arellomobile.example.MyPresenter mMyPresenter;
 *
 * }
 *
 * }
 * </pre>
 * <p>
 * PresenterBinderClassGenerator generates PresenterBinder
 * <p>
 *
 * @author Yuri Shmakov
 * @author Alexander Blinov
 */
public final class PresenterBinderClassGenerator extends JavaFilesGenerator<TargetClassInfo> {

	@Override
	public List<JavaFile> generate(TargetClassInfo targetClassInfo) {
		ClassName targetClassName = targetClassInfo.getName();
		List<TargetPresenterField> fields = targetClassInfo.getFields();

		final String containerSimpleName = String.join("$", targetClassName.simpleNames());

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(containerSimpleName + MvpProcessor.PRESENTER_BINDER_SUFFIX)
				.addModifiers(Modifier.PUBLIC)
				.superclass(ParameterizedTypeName.get(ClassName.get(PresenterBinder.class), targetClassName));

		for (TargetPresenterField field : fields) {
			classBuilder.addType(generatePresenterBinderClass(field, targetClassName));
		}

		classBuilder.addMethod(generateGetPresentersMethod(fields, targetClassName));

		JavaFile javaFile = JavaFile.builder(targetClassName.packageName(), classBuilder.build())
				.indent("\t")
				.build();
		return Collections.singletonList(javaFile);
	}

	private static MethodSpec generateGetPresentersMethod(List<TargetPresenterField> fields,
	                                                      ClassName containerClassName) {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("getPresenterFields")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(ParameterizedTypeName.get(
						ClassName.get(List.class), ParameterizedTypeName.get(
								ClassName.get(PresenterField.class), containerClassName)));

		builder.addStatement("$T<$T<$T>> presenters = new $T<>($L)",
				List.class, PresenterField.class, containerClassName,
				ArrayList.class, fields.size());

		for (TargetPresenterField field : fields) {
			builder.addStatement("presenters.add(new $L())", field.getGeneratedClassName());
		}

		builder.addStatement("return presenters");

		return builder.build();
	}

	private static TypeSpec generatePresenterBinderClass(TargetPresenterField field,
	                                                     ClassName targetClassName) {
		String tag = field.getTag();
		if (tag == null) tag = field.getName();

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(field.getGeneratedClassName())
				.addModifiers(Modifier.PUBLIC)
				.superclass(ParameterizedTypeName.get(
						ClassName.get(PresenterField.class), targetClassName))
				.addMethod(generatePresenterBinderConstructor(field, tag))
				.addMethod(generateBindMethod(field, targetClassName))
				.addMethod(generateProvidePresenterMethod(field, targetClassName));

		String tagProviderMethodName = field.getPresenterTagProviderMethodName();
		if (tagProviderMethodName != null) {
			classBuilder.addMethod(generateGetTagMethod(tagProviderMethodName, targetClassName));
		}

		return classBuilder.build();
	}

	private static MethodSpec generatePresenterBinderConstructor(TargetPresenterField field, String tag) {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addStatement("super($S, $T.$L, $S, $T.class)",
						tag,
						field.getPresenterType().getDeclaringClass(),
						field.getPresenterType().name(),
						field.getPresenterId(),
						field.getTypeName())
				.build();
	}

	private static MethodSpec generateBindMethod(TargetPresenterField field,
	                                             ClassName targetClassName) {
		return MethodSpec.methodBuilder("bind")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(targetClassName, "target")
				.addParameter(MvpPresenter.class, "presenter")
				.addStatement("target.$L = ($T) presenter", field.getName(), field.getTypeName())
				.build();
	}

	private static MethodSpec generateProvidePresenterMethod(TargetPresenterField field,
	                                                         ClassName targetClassName) {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("providePresenter")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(ParameterizedTypeName.get(
						ClassName.get(MvpPresenter.class), WildcardTypeName.subtypeOf(Object.class)))
				.addParameter(targetClassName, "delegated");

		if (field.getPresenterProviderMethodName() != null) {
			builder.addStatement("return delegated.$L()", field.getPresenterProviderMethodName());
		} else {
			boolean hasEmptyConstructor = Util.hasEmptyConstructor((TypeElement) ((DeclaredType) field.getClazz()).asElement());

			if (hasEmptyConstructor) {
				builder.addStatement("return new $T()", field.getTypeName());
			} else {
				builder.addStatement(
						"throw new $T($S + $S)", IllegalStateException.class,
						field.getClazz(), " has not default constructor. You can apply @ProvidePresenter to some method which will construct Presenter. Also you can make it default constructor");
			}
		}

		return builder.build();
	}

	private static MethodSpec generateGetTagMethod(String tagProviderMethodName,
	                                               ClassName targetClassName) {
		return MethodSpec.methodBuilder("getTag")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(String.class)
				.addParameter(targetClassName, "delegated")
				.addStatement("return String.valueOf(delegated.$L())", tagProviderMethodName)
				.build();
	}
}
