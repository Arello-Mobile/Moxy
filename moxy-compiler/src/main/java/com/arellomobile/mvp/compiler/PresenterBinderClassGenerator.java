package com.arellomobile.mvp.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

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
 * @author Alexander Blinov
 * @author Yuri Shmakov
 */
final class PresenterBinderClassGenerator extends ClassGenerator<VariableElement> {
	public static final String PRESENTER_FIELD_ANNOTATION = InjectPresenter.class.getName();
	public static final String PROVIDE_PRESENTER = ProvidePresenter.class.getName();
	private final List<String> mPresentersContainers;

	public PresenterBinderClassGenerator() {
		mPresentersContainers = new ArrayList<>();
	}

	public boolean generate(VariableElement variableElement, List<ClassGeneratingParams> classGeneratingParamsList) {
		final Element enclosingElement = variableElement.getEnclosingElement();

		if (!(enclosingElement instanceof TypeElement)) {
			throw new RuntimeException("Only class fields could be annotated as @InjectPresenter: " + variableElement + " at " + enclosingElement);
		}
		if (mPresentersContainers.contains(enclosingElement.toString())) {
			return false;
		}

		TypeElement presentersContainer = (TypeElement) enclosingElement;

		System.out.println(presentersContainer + " " + presentersContainer.getModifiers().iterator().next().name());

		mPresentersContainers.add(presentersContainer.toString());

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

		List<PresenterProvider> presenterProviders = collectProviders(presentersContainer);

		bindProvidersToFields(fields, presenterProviders);

		MvpCompiler.getMessager().printMessage(Diagnostic.Kind.NOTE, presenterProviders.toString());

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
					MvpCompiler.getMessager().printMessage(Diagnostic.Kind.NOTE, field.toString());
					field.setPresenterProviderMethodName(presenterProvider.mName);
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

						if ("type()".equals(executableElement.toString())) {
							type = elementValues.get(executableElement).getValue().toString();
						}

						if ("tag()".equals(executableElement.toString())) {
							tag = elementValues.get(executableElement).toString();
						}

						if ("presenterId()".equals(executableElement.toString())) {
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

	private List<PresenterProvider> collectProviders(TypeElement presentersContainer) {
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

						if ("type()".equals(executableElement.toString())) {
							type = elementValues.get(executableElement).getValue().toString();
						}

						if ("tag()".equals(executableElement.toString())) {
							tag = elementValues.get(executableElement).toString();
						}

						if ("presenterId()".equals(executableElement.toString())) {
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
		String s = "\tpublic class " + field.getGeneratedClassName() + " extends PresenterField {\n" +
		           "\t\tpublic " + field.getGeneratedClassName() + "() {\n" +
		           "\t\t\tsuper(" + field.getTag() + ", PresenterType." + field.getType().name() + ", " + field.getPresenterId() + ", " + field.getClazz() + ".class);\n" +
		           "\t\t}\n" +
		           "\n" +
		           "\t\t@Override\n" +
		           "\t\tpublic void setValue(MvpPresenter presenter) {\n" +
		           "\t\t\tmTarget." + field.getName() + " = (" + field.getClazz() + ") presenter;\n" +
		           "\t\t}\n";

		if (field.getPresenterProviderMethodName() != null) {
			s += "\t\t@Override\n" +
			     "\t\tpublic MvpPresenter<?> providePresenter() {\n" +
			     "\t\t\treturn mTarget." + field.getPresenterProviderMethodName() + "();\n" +
			     "\t\t}\n" +
			     "\t";
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
			return mName + MvpProcessor.VIEW_STATE_CLASS_NAME_PROVIDER_SUFFIX;
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

		@Override
		public String toString() {
			return "Field{" +
			       "mClazz=" + mClazz +
			       ", mName='" + mName + '\'' +
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
			       "mClazz=" + mClazz +
			       ", mName='" + mName + '\'' +
			       ", mType=" + mType +
			       ", mTag='" + mTag + '\'' +
			       ", mPresenterId='" + mPresenterId + '\'' +
			       '}';
		}
	}
}
