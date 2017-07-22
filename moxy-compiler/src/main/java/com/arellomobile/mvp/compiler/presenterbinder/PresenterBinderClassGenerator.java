package com.arellomobile.mvp.compiler.presenterbinder;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.PresenterBinder;
import com.arellomobile.mvp.compiler.ClassGeneratingParams;
import com.arellomobile.mvp.compiler.ClassGenerator;
import com.arellomobile.mvp.compiler.Util;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterField;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import static com.arellomobile.mvp.compiler.Util.join;

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
public final class PresenterBinderClassGenerator extends ClassGenerator<VariableElement> {
	private static final String PRESENTER_FIELD_ANNOTATION = InjectPresenter.class.getName();
	private static final String PROVIDE_PRESENTER_ANNOTATION = ProvidePresenter.class.getName();
	private static final String PROVIDE_PRESENTER_TAG_ANNOTATION = ProvidePresenterTag.class.getName();
	private final Set<TypeElement> mPresentersContainers = new HashSet<>();

	public Set<TypeElement> getPresentersContainers() {
		return mPresentersContainers;
	}

	@Override
	public boolean generate(VariableElement variableElement,
	                        List<ClassGeneratingParams> classGeneratingParamsList) {
		final Element enclosingElement = variableElement.getEnclosingElement();

		if (!(enclosingElement instanceof TypeElement)) {
			throw new RuntimeException("Only class fields could be annotated as @InjectPresenter: " +
					variableElement + " at " + enclosingElement);
		}

		if (mPresentersContainers.contains(enclosingElement)) {
			return false;
		}

		final TypeElement presentersContainer = (TypeElement) enclosingElement;
		mPresentersContainers.add(presentersContainer);

		final ClassName targetClassName = ClassName.get(presentersContainer);

		// gather presenter fields info
		List<TargetPresenterField> fields = collectFields(presentersContainer);
		bindProvidersToFields(fields, collectPresenterProviders(presentersContainer));
		bindTagProvidersToFields(fields, collectTagProviders(presentersContainer));

		JavaFile javaFile = generateFile(targetClassName, fields);

		ClassGeneratingParams classGeneratingParams = new ClassGeneratingParams();
		classGeneratingParams.setName(targetClassName.reflectionName() + MvpProcessor.PRESENTER_BINDER_SUFFIX);
		classGeneratingParams.setBody(javaFile.toString());
		classGeneratingParamsList.add(classGeneratingParams);

		return true;
	}

	private static List<TargetPresenterField> collectFields(TypeElement presentersContainer) {
		List<TargetPresenterField> fields = new ArrayList<>();

		for (Element element : presentersContainer.getEnclosedElements()) {
			if (element.getKind() != ElementKind.FIELD) {
				continue;
			}

			AnnotationMirror annotation = Util.getAnnotation(element, PRESENTER_FIELD_ANNOTATION);

			if (annotation == null) {
				continue;
			}

			// TODO: simplify?
			TypeMirror clazz = ((DeclaredType) element.asType()).asElement().asType();

			String name = element.toString();

			Map<String, AnnotationValue> values = Util.getAnnotationValues(annotation);
			AnnotationValue typeValue = values.get("type");
			AnnotationValue tagValue = values.get("tag");
			AnnotationValue presenterIdValue = values.get("presenterId");

			String type = typeValue != null ? typeValue.getValue().toString() : null;
			String tag = tagValue != null ? tagValue.getValue().toString() : null;
			String presenterId = presenterIdValue != null ? presenterIdValue.getValue().toString() : null;

			TargetPresenterField field = new TargetPresenterField(clazz, name, type, tag, presenterId);
			fields.add(field);
		}
		return fields;
	}

	private static List<PresenterProviderMethod> collectPresenterProviders(TypeElement presentersContainer) {
		List<PresenterProviderMethod> providers = new ArrayList<>();

		for (Element element : presentersContainer.getEnclosedElements()) {
			if (element.getKind() != ElementKind.METHOD) {
				continue;
			}

			final ExecutableElement providerMethod = (ExecutableElement) element;

			final AnnotationMirror annotation = Util.getAnnotation(element, PROVIDE_PRESENTER_ANNOTATION);

			if (annotation == null) {
				continue;
			}

			final String name = providerMethod.getSimpleName().toString();
			final DeclaredType kind = ((DeclaredType) providerMethod.getReturnType());

			Map<String, AnnotationValue> values = Util.getAnnotationValues(annotation);

			final AnnotationValue typeValue = values.get("type");
			final AnnotationValue tagValue = values.get("tag");
			final AnnotationValue presenterIdValue = values.get("presenterId");

			final String type = typeValue != null ? typeValue.getValue().toString() : null;
			final String tag = tagValue != null ? tagValue.getValue().toString() : null;
			final String presenterId = presenterIdValue != null ? presenterIdValue.getValue().toString() : null;


			PresenterProviderMethod provider = new PresenterProviderMethod(kind, name, type, tag, presenterId);
			providers.add(provider);
		}
		return providers;
	}

	private static List<TagProviderMethod> collectTagProviders(TypeElement presentersContainer) {
		List<TagProviderMethod> providers = new ArrayList<>();

		for (Element element : presentersContainer.getEnclosedElements()) {
			if (element.getKind() != ElementKind.METHOD) {
				continue;
			}

			final ExecutableElement providerMethod = (ExecutableElement) element;

			final AnnotationMirror annotation = Util.getAnnotation(element, PROVIDE_PRESENTER_TAG_ANNOTATION);

			if (annotation == null) {
				continue;
			}

			final String name = providerMethod.getSimpleName().toString();

			Map<String, AnnotationValue> values = Util.getAnnotationValues(annotation);
			final AnnotationValue typeValue = values.get("type");
			final AnnotationValue presenterIdValue = values.get("presenterId");

			final TypeMirror presenterClass = (TypeMirror) values.get("presenterClass").getValue();
			final String type = typeValue != null ? typeValue.getValue().toString() : null;
			final String presenterId = presenterIdValue != null ? presenterIdValue.getValue().toString() : null;

			TagProviderMethod provider = new TagProviderMethod(presenterClass, name, type, presenterId);
			providers.add(provider);
		}
		return providers;
	}

	private static void bindProvidersToFields(List<TargetPresenterField> fields,
	                                          List<PresenterProviderMethod> presenterProviders) {
		if (fields.isEmpty() || presenterProviders.isEmpty()) {
			return;
		}

		for (PresenterProviderMethod presenterProvider : presenterProviders) {
			TypeMirror providerTypeMirror = presenterProvider.getClazz().asElement().asType();

			for (TargetPresenterField field : fields) {
				if ((field.getClazz()).equals(providerTypeMirror)) {
					if (field.getType() != presenterProvider.getPresenterType()) {
						continue;
					}

					if (field.getTag() == null && presenterProvider.getTag() != null) {
						continue;
					}
					if (field.getTag() != null && !field.getTag().equals(presenterProvider.getTag())) {
						continue;
					}

					if (field.getPresenterId() == null && presenterProvider.getPresenterId() != null) {
						continue;
					}
					if (field.getPresenterId() != null && !field.getPresenterId().equals(presenterProvider.getPresenterId())) {
						continue;
					}

					field.setPresenterProviderMethodName(presenterProvider.getName());
				}
			}

		}
	}

	private static void bindTagProvidersToFields(List<TargetPresenterField> fields,
	                                             List<TagProviderMethod> tagProviders) {
		if (fields.isEmpty() || tagProviders.isEmpty()) {
			return;
		}
		for (TagProviderMethod tagProvider : tagProviders) {
			for (TargetPresenterField field : fields) {
				if ((field.getClazz()).equals(tagProvider.getPresenterClass())) {
					if (field.getType() != tagProvider.getType()) {
						continue;
					}

					if (field.getPresenterId() == null && tagProvider.getPresenterId() != null) {
						continue;
					}
					if (field.getPresenterId() != null && !field.getPresenterId().equals(tagProvider.getPresenterId())) {
						continue;
					}

					field.setPresenterTagProviderMethodName(tagProvider.getMethodName());
				}
			}

		}
	}


	private static JavaFile generateFile(ClassName targetClassName,
	                                     List<TargetPresenterField> fields) {
		final String containerSimpleName = join("$", targetClassName.simpleNames());
		final String containerPackageName = targetClassName.packageName();

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(containerSimpleName + MvpProcessor.PRESENTER_BINDER_SUFFIX)
				.addModifiers(Modifier.PUBLIC)
				.superclass(ParameterizedTypeName.get(ClassName.get(PresenterBinder.class), targetClassName));

		for (TargetPresenterField field : fields) {
			classBuilder.addType(generatePresenterBinderClass(field, targetClassName));
		}

		classBuilder.addMethod(generateGetPresentersMethod(fields, targetClassName));

		return JavaFile.builder(containerPackageName, classBuilder.build())
				.indent("\t")
				.build();
	}

	private static MethodSpec generateGetPresentersMethod(List<TargetPresenterField> fields,
	                                                      ClassName containerClassName) {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("getPresenterFields")
				.addModifiers(Modifier.PUBLIC)
				.returns(ParameterizedTypeName.get(
						ClassName.get(List.class), ParameterizedTypeName.get(
								ClassName.get(PresenterField.class), containerClassName)));

		builder.addStatement(
				"List<PresenterField<$T>> presenters = new $T<>()",
				containerClassName, ArrayList.class);

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
						ClassName.get(PresenterField.class), targetClassName));

		classBuilder.addMethod(MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addStatement("super($S, $T.$L, $S, $T.class)",
						tag,
						field.getType().getDeclaringClass(),
						field.getType().name(),
						field.getPresenterId(),
						field.getTypeName())
				.build());

		classBuilder.addMethod(generateBindMethod(field, targetClassName));
		classBuilder.addMethod(generateProvidePresenterMethod(field, targetClassName));

		String tagProviderMethodName = field.getPresenterTagProviderMethodName();
		if (tagProviderMethodName != null) {
			classBuilder.addMethod(generateGetTagMethod(tagProviderMethodName, targetClassName));
		}

		return classBuilder.build();
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
			builder
					.addStatement("return delegated.$L()", field.getPresenterProviderMethodName());
		} else {
			boolean hasEmptyConstructor = Util.hasEmptyConstructor((TypeElement) ((DeclaredType) field.getClazz()).asElement());

			if (hasEmptyConstructor) {
				builder.addStatement("return new $T()", field.getTypeName());
			} else {
				builder.addStatement(
						"throw new IllegalStateException($S + $S)",
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