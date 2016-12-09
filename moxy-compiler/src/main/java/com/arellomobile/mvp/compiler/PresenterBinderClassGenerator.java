package com.arellomobile.mvp.compiler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

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
final class PresenterBinderClassGenerator extends ClassGenerator<VariableElement> {
	public static final String PRESENTER_FIELD_ANNOTATION = InjectPresenter.class.getName();
	public static final String PROVIDE_PRESENTER = ProvidePresenter.class.getName();
	public static final String PROVIDE_PRESENTER_TAG = ProvidePresenterTag.class.getName();
	private final Set<TypeElement> mPresentersContainers;

	public PresenterBinderClassGenerator() {
		mPresentersContainers = new HashSet<>();
	}

	public boolean generate(VariableElement variableElement, List<ClassGeneratingParams> classGeneratingParamsList) {
		final Element enclosingElement = variableElement.getEnclosingElement();

		if (!(enclosingElement instanceof TypeElement)) {
			throw new RuntimeException("Only class fields could be annotated as @InjectPresenter: " + variableElement + " at " + enclosingElement);
		}

		if (mPresentersContainers.contains(enclosingElement)) {
			return false;
		}

		TypeElement presentersContainer = (TypeElement) enclosingElement;

		mPresentersContainers.add(presentersContainer);

		String fullClassName = Util.getFullClassName(presentersContainer);

		ClassGeneratingParams classGeneratingParams = new ClassGeneratingParams();
		classGeneratingParams.setName(fullClassName + MvpProcessor.PRESENTER_BINDER_SUFFIX);

		String parentClassName = presentersContainer.toString();

		final String viewClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);

		String builder = "package " + fullClassName.substring(0, fullClassName.lastIndexOf(".")) + ";\n" +
		                 "\n" +
		                 "import java.util.ArrayList;\n" +
		                 "import java.util.List;\n" +
		                 "\n" +
		                 "import com.arellomobile.mvp.PresenterBinder;\n" +
		                 "import com.arellomobile.mvp.presenter.PresenterField;\n" +
		                 "import com.arellomobile.mvp.MvpPresenter;\n" +
		                 "import com.arellomobile.mvp.presenter.PresenterType;\n" +
		                 "\n" +
		                 "public class " + viewClassName + MvpProcessor.PRESENTER_BINDER_SUFFIX + " extends PresenterBinder<" + parentClassName + "> {\n";

		List<Field> fields = collectFields(presentersContainer);

		List<PresenterProvider> presenterProviders = collectPresenterProviders(presentersContainer);

		List<TagProvider> tagProviders = collectTagProviders(presentersContainer);

		bindProvidersToFields(fields, presenterProviders);

		bindTagProvidersToFields(fields, tagProviders);

		for (Field field : fields) {
			builder = generatePresenterBinderClass(builder, field);
		}

		builder = generateGetPresentersMethod(builder, fields, parentClassName);

		builder += "}\n";

		classGeneratingParams.setBody(builder);
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
				if ((field.mClazz).equals(providerTypeMirror)) {
					if (field.mType != presenterProvider.mType) {
						continue;
					}

					if (field.mTag == null && presenterProvider.mTag != null) {
						continue;
					}
					if (field.mTag != null && !field.mTag.equals(presenterProvider.mTag)) {
						continue;
					}

					if (field.mPresenterId == null && presenterProvider.mPresenterId != null) {
						continue;
					}
					if (field.mPresenterId != null && !field.mPresenterId.equals(presenterProvider.mPresenterId)) {
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
			TypeMirror providerTypeMirror = tagProvider.mPresenterClass.asElement().asType();
			for (Field field : fields) {
				if ((field.mClazz).equals(providerTypeMirror)) {
					if (field.mType != tagProvider.mType) {
						continue;
					}

					if (field.mPresenterId == null && tagProvider.mPresenterId != null) {
						continue;
					}
					if (field.mPresenterId != null && !field.mPresenterId.equals(tagProvider.mPresenterId)) {
						continue;
					}

					field.setPresenterTagProviderMethodName(tagProvider.mMethodName);
				}
			}

		}
	}

	private List<Field> collectFields(TypeElement presentersContainer) {
		List<Field> fields = new ArrayList<>();

		outer:
		for (Element element : presentersContainer.getEnclosedElements()) {
			if (!(element instanceof VariableElement)) {
				continue;
			}

			final VariableElement presenterFieldElement = (VariableElement) element;

			for (AnnotationMirror annotationMirror : presenterFieldElement.getAnnotationMirrors()) {
				if (annotationMirror.getAnnotationType().asElement().toString().equals(PRESENTER_FIELD_ANNOTATION)) {
					String type = null;
					String tag = null;
					String presenterId = null;

					final String name = element.toString();
					TypeMirror clazz = ((DeclaredType) element.asType()).asElement().asType();

					final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();

					final Set<? extends ExecutableElement> keySet = elementValues.keySet();

					for (ExecutableElement executableElement : keySet) {
						String key = executableElement.getSimpleName().toString();

						if ("type".equals(key)) {
							type = elementValues.get(executableElement).getValue().toString();
						} else if ("tag".equals(key)) {
							tag = elementValues.get(executableElement).toString();
						} else if ("presenterId".equals(key)) {
							presenterId = elementValues.get(executableElement).toString();
						}
					}
					Field field = new Field(clazz, name, type, tag, presenterId);
					fields.add(field);
					continue outer;
				}
			}
		}
		return fields;
	}

	private List<PresenterProvider> collectPresenterProviders(TypeElement presentersContainer) {
		List<PresenterProvider> providers = new ArrayList<>();

		outer:
		for (Element element : presentersContainer.getEnclosedElements()) {
			if (!(element instanceof ExecutableElement)) {
				continue;
			}

			final ExecutableElement providerMethod = (ExecutableElement) element;

			for (AnnotationMirror annotationMirror : providerMethod.getAnnotationMirrors()) {
				if (annotationMirror.getAnnotationType().asElement().toString().equals(PROVIDE_PRESENTER)) {
					if (providerMethod.getReturnType().getKind() != TypeKind.DECLARED) {
						continue;
					}

					DeclaredType kind = ((DeclaredType) providerMethod.getReturnType());
					String type = null;
					String tag = null;
					String presenterId = null;

					final String name = providerMethod.getSimpleName().toString();

					final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();

					final Set<? extends ExecutableElement> keySet = elementValues.keySet();

					for (ExecutableElement executableElement : keySet) {
						String key = executableElement.getSimpleName().toString();

						if ("type".equals(key)) {
							type = elementValues.get(executableElement).getValue().toString();
						} else if ("tag".equals(key)) {
							tag = elementValues.get(executableElement).toString();
						} else if ("presenterId".equals(key)) {
							presenterId = elementValues.get(executableElement).toString();
						}
					}
					PresenterProvider provider = new PresenterProvider(kind, name, type, tag, presenterId);
					providers.add(provider);
					continue outer;
				}
			}
		}
		return providers;
	}

	private List<TagProvider> collectTagProviders(TypeElement presentersContainer) {
		List<TagProvider> providers = new ArrayList<>();

		outer:
		for (Element element : presentersContainer.getEnclosedElements()) {
			if (!(element instanceof ExecutableElement)) {
				continue;
			}

			final ExecutableElement providerMethod = (ExecutableElement) element;

			for (AnnotationMirror annotationMirror : providerMethod.getAnnotationMirrors()) {
				if (annotationMirror.getAnnotationType().asElement().toString().equals(PROVIDE_PRESENTER_TAG)) {
					if (providerMethod.getReturnType().getKind() != TypeKind.DECLARED) {
						continue;
					}

					DeclaredType kind = null;
					String type = null;
					String presenterId = null;

					final String name = providerMethod.getSimpleName().toString();

					final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();

					final Set<? extends ExecutableElement> keySet = elementValues.keySet();

					for (ExecutableElement executableElement : keySet) {
						String key = executableElement.getSimpleName().toString();

						if ("presenterClass".equals(key)) {
							kind = (DeclaredType) elementValues.get(executableElement).getValue();
						} else if ("type".equals(key)) {
							type = elementValues.get(executableElement).getValue().toString();
						} else if ("presenterId".equals(key)) {
							presenterId = elementValues.get(executableElement).toString();
						}
					}

					TagProvider provider = new TagProvider(kind, name, type, presenterId);
					providers.add(provider);
					continue outer;
				}
			}
		}
		return providers;
	}

	public Set<TypeElement> getPresentersContainers() {
		return mPresentersContainers;
	}

	private static String generateGetPresentersMethod(final String builder, final List<Field> fields, String parentClassName) {
		String s = "\tpublic List<PresenterField<?, ? super " + parentClassName + ">> getPresenterFields() {\n" +
		           "\t\tList<PresenterField<?, ? super " + parentClassName + ">> presenters = new ArrayList<>();\n" +
		           "\n";


		for (Field field : fields) {
			s += "\t\tpresenters.add(new " + field.getGeneratedClassName() + "());\n";
		}

		s += "\n" +
		     "\t\treturn presenters;\n" +
		     "\t}\n" +
		     "\n";

		return builder + s;
	}

	private static String generatePresenterBinderClass(final String builder, final Field field) {
		TypeElement clazz = (TypeElement) ((DeclaredType) field.getClazz()).asElement();
		String s = "\tpublic class " + field.getGeneratedClassName() + " extends PresenterField {\n" +
		           "\t\tpublic " + field.getGeneratedClassName() + "() {\n" +
		           "\t\t\tsuper(" + field.getTag() + ", PresenterType." + field.getType().name() + ", " + field.getPresenterId() + ", " + clazz + ".class);\n" +
		           "\t\t}\n" +
		           "\n" +
		           "\t\t@Override\n" +
		           "\t\tpublic void setValue(MvpPresenter presenter) {\n" +
		           "\t\t\tmTarget." + field.getName() + " = (" + clazz.getQualifiedName() + ") presenter;\n" +
		           "\t\t}\n";

			s += "\n" +
			     "\t\t@Override\n" +
			     "\t\tpublic MvpPresenter<?> providePresenter() {\n";
		if (field.getPresenterProviderMethodName() != null) {
			s+= "\t\t\treturn mTarget." + field.getPresenterProviderMethodName() + "();\n";
		} else {
			boolean hasEmptyConstructor = false;
			List<? extends Element> enclosedElements = clazz.getEnclosedElements();
			for (Element enclosedElement : enclosedElements) {
				if (enclosedElement.getKind() == ElementKind.CONSTRUCTOR) {
					List<? extends VariableElement> parameters = ((ExecutableElement) enclosedElement).getParameters();
					if (parameters == null || parameters.isEmpty()) {
						hasEmptyConstructor = true;
						break;
					}
				}
			}
			if (hasEmptyConstructor) {
			s += "\t\t\treturn new " + clazz.getQualifiedName() + "();\n";
			} else {
			s += "\t\t\tthrow new IllegalStateException(\"" + clazz.getSimpleName() + " has not default constructor. You can apply @ProvidePresenter to some method which will construct Presenter. Also you can make it default constructor\");\n";
			}
		}
		    s += "\t\t}\n";

		if (field.getPresenterTagProviderMethodName() != null) {
			s += "\n" +
			     "\t\t@Override\n" +
			     "\t\tpublic String getTag() {\n" +
			     "\t\t\treturn String.valueOf(mTarget." + field.getPresenterTagProviderMethodName() + "());\n" +
			     "\t\t}\n";
		}

		s += "\t}\n" +
		     "\n";
		return builder + s;
	}

	private static class Field {
		private final TypeMirror mClazz;
		private final String mName;
		private final PresenterType mType;
		private final String mTag;
		private final String mPresenterId;

		private String mPresenterProviderMethodName;
		private String mPresenterTagProviderMethodName;

		Field(final TypeMirror clazz, final String name, final String type, final String tag, String presenterId) {
			mClazz = clazz;
			mName = name;
			mTag = tag;


			if (type == null) {
				mType = PresenterType.LOCAL;
			} else {
				mType = PresenterType.valueOf(type);
			}

			mPresenterId = presenterId;
		}

		public TypeMirror getClazz() {
			return mClazz;
		}

		public String getGeneratedClassName() {
			return mName + MvpProcessor.PRESENTER_BINDER_INNER_SUFFIX;
		}

		public String getTag() {
			return mTag;
		}

		public String getName() {
			return mName;
		}

		public PresenterType getType() {
			return mType;
		}

		public String getPresenterId() {
			return mPresenterId;
		}

		public String getPresenterProviderMethodName() {
			return mPresenterProviderMethodName;
		}

		public void setPresenterProviderMethodName(String presenterProviderMethodName) {
			mPresenterProviderMethodName = presenterProviderMethodName;
		}

		public String getPresenterTagProviderMethodName() {
			return mPresenterTagProviderMethodName;
		}

		public void setPresenterTagProviderMethodName(String presenterTagProviderMethodName) {
			mPresenterTagProviderMethodName = presenterTagProviderMethodName;
		}

		@Override
		public String toString() {
			return "Field{" +
			       "mPresenterClass=" + mClazz +
			       ", mMethodName='" + mName + '\'' +
			       ", mType=" + mType +
			       ", mTag='" + mTag + '\'' +
			       ", mPresenterId='" + mPresenterId + '\'' +
			       ", mPresenterProviderMethodName='" + mPresenterProviderMethodName + '\'' +
			       '}';
		}
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

		@Override
		public String toString() {
			return "PresenterProvider{" +
			       "mPresenterClass=" + mClazz +
			       ", mMethodName='" + mName + '\'' +
			       ", mType=" + mType +
			       ", mTag='" + mTag + '\'' +
			       ", mPresenterId='" + mPresenterId + '\'' +
			       '}';
		}
	}

	private class TagProvider {
		private final DeclaredType mPresenterClass;
		private final String mMethodName;
		private final PresenterType mType;
		private final String mPresenterId;

		public TagProvider(DeclaredType presenterClass, String methodName, String type, String presenterId) {
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
