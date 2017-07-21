package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.PresenterBinder;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterField;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
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
			throw new RuntimeException("Only class fields could be annotated as @InjectPresenter: " + variableElement + " at " + enclosingElement);
		}

		if (mPresentersContainers.contains(enclosingElement)) {
			return false;
		}

		final TypeElement presentersContainer = (TypeElement) enclosingElement;
		mPresentersContainers.add(presentersContainer);

		final ClassName presentersContainerClassName = ClassName.get(presentersContainer);

		final String containerSimpleName = join("$", presentersContainerClassName.simpleNames());
		final String containerPackageName = presentersContainerClassName.packageName();

		List<Field> fields = collectFields(presentersContainer);
		bindProvidersToFields(fields, collectPresenterProviders(presentersContainer));
		bindTagProvidersToFields(fields, collectTagProviders(presentersContainer));

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(containerSimpleName + MvpProcessor.PRESENTER_BINDER_SUFFIX)
				.addModifiers(Modifier.PUBLIC)
				.superclass(ParameterizedTypeName.get(ClassName.get(PresenterBinder.class), presentersContainerClassName));

		for (Field field : fields) {
			classBuilder.addType(generatePresenterBinderClass(field, presentersContainerClassName));
		}

		classBuilder.addMethod(generateGetPresentersMethod(fields, presentersContainerClassName));

		JavaFile javaFile = JavaFile.builder(containerPackageName, classBuilder.build())
				.indent("\t")
				.build();

		ClassGeneratingParams classGeneratingParams = new ClassGeneratingParams();
		classGeneratingParams.setName(containerPackageName + "." + containerSimpleName + MvpProcessor.PRESENTER_BINDER_SUFFIX);
		classGeneratingParams.setBody(javaFile.toString());
		classGeneratingParamsList.add(classGeneratingParams);

		return true;
	}

	private void bindProvidersToFields(List<Field> fields, List<PresenterProvider> presenterProviders) {
		if (fields.isEmpty() || presenterProviders.isEmpty()) {
			return;
		}

		for (PresenterProvider presenterProvider : presenterProviders) {
			TypeMirror providerTypeMirror = presenterProvider.mClazz.asElement().asType();

			for (Field field : fields) {
				if ((field.getTypeMirror()).equals(providerTypeMirror)) {
					if (field.getType() != presenterProvider.mType) {
						continue;
					}

					if (field.getTag() == null && presenterProvider.mTag != null) {
						continue;
					}
					if (field.getTag() != null && !field.getTag().equals(presenterProvider.mTag)) {
						continue;
					}

					if (field.getPresenterId() == null && presenterProvider.mPresenterId != null) {
						continue;
					}
					if (field.getPresenterId() != null && !field.getPresenterId().equals(presenterProvider.mPresenterId)) {
						continue;
					}

					field.setPresenterProviderMethodName(presenterProvider.mName);
				}
			}

		}
	}

	private void bindTagProvidersToFields(List<Field> fields, List<TagProvider> tagProviders) {
		if (fields.isEmpty() || tagProviders.isEmpty()) {
			return;
		}
		for (TagProvider tagProvider : tagProviders) {
			for (Field field : fields) {
				if ((field.getTypeMirror()).equals(tagProvider.mPresenterClass)) {
					if (field.getType() != tagProvider.mType) {
						continue;
					}

					if (field.getPresenterId() == null && tagProvider.mPresenterId != null) {
						continue;
					}
					if (field.getPresenterId() != null && !field.getPresenterId().equals(tagProvider.mPresenterId)) {
						continue;
					}

					field.setPresenterTagProviderMethodName(tagProvider.mMethodName);
				}
			}

		}
	}

	private List<Field> collectFields(TypeElement presentersContainer) {
		List<Field> fields = new ArrayList<>();

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

			Field field = new Field(clazz, name, type, tag, presenterId);
			fields.add(field);
		}
		return fields;
	}

	private List<PresenterProvider> collectPresenterProviders(TypeElement presentersContainer) {
		List<PresenterProvider> providers = new ArrayList<>();

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


			PresenterProvider provider = new PresenterProvider(kind, name, type, tag, presenterId);
			providers.add(provider);
		}
		return providers;
	}

	private List<TagProvider> collectTagProviders(TypeElement presentersContainer) {
		List<TagProvider> providers = new ArrayList<>();

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

			TagProvider provider = new TagProvider(presenterClass, name, type, presenterId);
			providers.add(provider);
		}
		return providers;
	}

	private static MethodSpec generateGetPresentersMethod(final List<Field> fields, ClassName containerClassName) {
		MethodSpec.Builder builder = MethodSpec.methodBuilder("getPresenterFields")
				.addModifiers(Modifier.PUBLIC)
				.returns(ParameterizedTypeName.get(
						ClassName.get(List.class), ParameterizedTypeName.get(
								ClassName.get(PresenterField.class), containerClassName)));

		builder.addStatement("List<PresenterField<$T>> presenters = new $T<>()", containerClassName, ArrayList.class);

		for (Field field : fields) {
			builder.addStatement("presenters.add(new $L())", field.getGeneratedClassName());
		}

		builder.addStatement("return presenters");

		return builder.build();
	}

	private static TypeSpec generatePresenterBinderClass(final Field field, final ClassName targetClassName) {
		TypeName fieldType = TypeName.get(field.getTypeMirror());
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
						fieldType)
				.build());

		classBuilder.addMethod(MethodSpec.methodBuilder("bind")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(targetClassName, "target")
				.addParameter(MvpPresenter.class, "presenter")
				.addStatement("target.$L = ($T) presenter", field.getName(), fieldType)
				.build());

		MethodSpec.Builder providePresenterBuilder = MethodSpec.methodBuilder("providePresenter")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(ParameterizedTypeName.get(
						ClassName.get(MvpPresenter.class), WildcardTypeName.subtypeOf(Object.class)))
				.addParameter(targetClassName, "delegated");

		if (field.getPresenterProviderMethodName() != null) {
			providePresenterBuilder
					.addStatement("return delegated.$L()", field.getPresenterProviderMethodName());
		} else {
			boolean hasEmptyConstructor = Util.hasEmptyConstructor((TypeElement) ((DeclaredType) field.getTypeMirror()).asElement());

			if (hasEmptyConstructor) {
				providePresenterBuilder.addStatement("return new $T()", fieldType);
			} else {
				providePresenterBuilder.addStatement(
						"throw new IllegalStateException(\"$L has not default constructor. " +
								"You can apply @ProvidePresenter to some method which will construct Presenter. " +
								"Also you can make it default constructor\")", fieldType.toString());
			}
		}

		classBuilder.addMethod(providePresenterBuilder.build());

		if (field.getPresenterTagProviderMethodName() != null) {
			classBuilder.addMethod(MethodSpec.methodBuilder("getTag")
					.addAnnotation(Override.class)
					.addModifiers(Modifier.PUBLIC)
					.returns(String.class)
					.addParameter(targetClassName, "delegated")
					.addStatement("return String.valueOf(delegated.$L())", field.getPresenterTagProviderMethodName())
					.build());
		}

		return classBuilder.build();
	}

	private class PresenterProvider {
		private final DeclaredType mClazz;
		private final String mName;
		private final PresenterType mType;
		private final String mTag;
		private final String mPresenterId;

		public PresenterProvider(DeclaredType clazz, String name, String type, String tag, String presenterId) {
			mClazz = clazz;
			mName = name;
			if (type == null) {
				mType = PresenterType.LOCAL;
			} else {
				mType = PresenterType.valueOf(type);
			}
			mTag = tag;
			mPresenterId = presenterId;
		}
	}

	private class TagProvider {
		private final TypeMirror mPresenterClass;
		private final String mMethodName;
		private final PresenterType mType;
		private final String mPresenterId;

		public TagProvider(TypeMirror presenterClass, String methodName, String type, String presenterId) {
			mPresenterClass = presenterClass;
			mMethodName = methodName;
			if (type == null) {
				mType = PresenterType.LOCAL;
			} else {
				mType = PresenterType.valueOf(type);
			}
			mPresenterId = presenterId;
		}
	}
}

class Field {
	private final TypeMirror typeMirror;
	private final TypeName typeName;
	private final String name;
	private final PresenterType type;
	private final String tag;
	private final String presenterId;

	private String presenterProviderMethodName;
	private String presenterTagProviderMethodName;

	Field(TypeMirror typeMirror, String name, String type, String tag, String presenterId) {
		this.typeMirror = typeMirror;
		this.typeName = TypeName.get(typeMirror);
		this.name = name;
		this.tag = tag;


		if (type == null) {
			this.type = PresenterType.LOCAL;
		} else {
			this.type = PresenterType.valueOf(type);
		}

		this.presenterId = presenterId;
	}

	TypeMirror getTypeMirror() {
		return typeMirror;
	}

	TypeName getTypeName() {
		return typeName;
	}

	String getGeneratedClassName() {
		return name + MvpProcessor.PRESENTER_BINDER_INNER_SUFFIX;
	}

	String getTag() {
		return tag;
	}

	String getName() {
		return name;
	}

	PresenterType getType() {
		return type;
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
