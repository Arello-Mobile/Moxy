package com.arellomobile.mvp.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.arellomobile.mvp.DefaultParamsHolder;
import com.arellomobile.mvp.DefaultPresenterFactory;
import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
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
 * @author Alexander Blinov
 */
final class PresenterBinderClassGenerator extends ClassGenerator<VariableElement>
{
	public static final String PRESENTER_FIELD_ANNOTATION = InjectPresenter.class.getName();
	private final List<String> mPresentersContainers;

	public PresenterBinderClassGenerator()
	{
		mPresentersContainers = new ArrayList<>();
	}

	public boolean generate(VariableElement variableElement, List<ClassGeneratingParams> classGeneratingParamsList)
	{
		final Element enclosingElement = variableElement.getEnclosingElement();

		if (!(enclosingElement instanceof TypeElement))
		{
			throw new RuntimeException("Only class fields could be annotated as @InjectPresenter: " + variableElement + " at " + enclosingElement);
		}
		if (mPresentersContainers.contains(enclosingElement.toString()))
		{
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
				"import com.arellomobile.mvp.ParamsHolder;" +
				"import com.arellomobile.mvp.PresenterBinder;\n" +
				"import com.arellomobile.mvp.presenter.PresenterField;\n" +
				"import com.arellomobile.mvp.PresenterFactory;\n" +
				"import com.arellomobile.mvp.MvpPresenter;\n" +
				"import com.arellomobile.mvp.presenter.PresenterType;\n" +
				"\n" +
				"public class " + viewClassName + MvpProcessor.PRESENTER_BINDER_SUFFIX + " implements PresenterBinder<" + parentClassName + ">" +
				"\n" +
				"{\n" +
				"\tprivate " + parentClassName + " mTarget;\n" +
				"\n" +
				"\n" +
				"\t@Override\n" +
				"\tpublic void setTarget(final " + parentClassName + " target)\n" +
				"\t{\n" +
				"\t\tmTarget = target;\n" +
				"\t}\n" +
				"\n";

		List<Field> fields = new ArrayList<>();

		outer:
		for (Element element : presentersContainer.getEnclosedElements())
		{
			if (!(element instanceof VariableElement))
			{
				continue;
			}

			final VariableElement presenterFieldElement = (VariableElement) element;

			for (AnnotationMirror annotationMirror : presenterFieldElement.getAnnotationMirrors())
			{
				if (annotationMirror.getAnnotationType().asElement().toString().equals(PRESENTER_FIELD_ANNOTATION))
				{
					String type = null;
					String tag = null;
					DeclaredType factory = null;
					String presenterId = null;

					final String name = element.toString();
					DeclaredType clazz = (DeclaredType) element.asType();

					final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();

					final Set<? extends ExecutableElement> keySet = elementValues.keySet();

					for (ExecutableElement executableElement : keySet)
					{

						if ("type()".equals(executableElement.toString()))
						{
							type = elementValues.get(executableElement).getValue().toString();
						}

						if ("tag()".equals(executableElement.toString()))
						{
							tag = elementValues.get(executableElement).toString();
						}

						if ("factory()".equals(executableElement.toString()))
						{
							factory = (DeclaredType) elementValues.get(executableElement).getValue();
						}

						if ("presenterId()".equals(executableElement.toString()))
						{
							presenterId = elementValues.get(executableElement).toString();
						}
					}
					Field field = new Field(clazz, name, type, tag, factory, presenterId);
					fields.add(field);
					continue outer;
				}
			}
		}

		for (Field field : fields)
		{
			builder = generatePresenterBinderClass(builder, field);
		}

		builder = generateGetPresentersMethod(builder, fields, parentClassName);

		builder += "}\n";

		classGeneratingParams.setBody(builder);
		classGeneratingParamsList.add(classGeneratingParams);

		return true;
	}

	private static String generateGetPresentersMethod(final String builder, final List<Field> fields, String parentClassName)
	{
		String s = "\n" +
				"\tpublic List<PresenterField<? super " + parentClassName + ">> getPresenterFields()\n" +
				"\t{\n" +
				"\t\tList<PresenterField<? super " + parentClassName + ">> presenters = new ArrayList<>();\n" +
				"\n";


		for (Field field : fields)
		{
			s += "\t\tpresenters.add(new " + field.getGeneratedClassName() + "());\n";
		}

		s += "\n" +
				"\t\treturn presenters;\n" +
				"\t}\n" +
				"\n";

		return builder + s;
	}

	private static String generatePresenterBinderClass(final String builder, final Field field)
	{
		boolean hasEmptyConstructor = false;

		for (Element element : ((TypeElement) field.getClazz().asElement()).getEnclosedElements())
		{
			if (element.getKind() != ElementKind.CONSTRUCTOR)
			{
				continue;
			}

			final ExecutableElement constructor = (ExecutableElement) element;

			if (!constructor.getModifiers().contains(Modifier.PUBLIC))
			{
				continue;
			}

			hasEmptyConstructor = constructor.getParameters().size() == 0;
			if (hasEmptyConstructor)
			{
				break;
			}
		}

		final String s = "\tpublic class " + field.getGeneratedClassName() + " implements PresenterField\n" +
				"\t{\n" +
				"\t\t@Override\n" +
				"\t\tpublic String getTag()\n" +
				"\t\t{\n" +
				"\t\t\treturn " + field.getTag() + ";\n" +
				"\t\t}\n" +
				"\n" +
				"\t\t@Override\n" +
				"\t\tpublic PresenterType getPresenterType()\n" +
				"\t\t{\n" +
				"\t\t\treturn PresenterType." + field.getType().name() + ";\n" +
				"\t\t}\n" +
				"\n" +
				"\t\t@Override\n" +
				"\t\tpublic void setValue(MvpPresenter presenter)\n" +
				"\t\t{\n" +
				"\t\t\tmTarget." + field.getName() + " = (" + field.getClazz() + ") presenter;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\t@Override\n" +
				"\t\tpublic Class<? extends MvpPresenter> getPresenterClass()\n" +
				"\t\t{\n" +
				"\t\t\treturn " + field.getClazz().asElement() + ".class;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\t@Override\n" +
				"\t\tpublic " + field.getClazz() + " getDefaultInstance()\n" +
				"\t\t{\n" +
				"\t\t\treturn " + (hasEmptyConstructor ?
					("new " + field.getClazz() + "()" )
						:
					"null") +
						";\n" +
				"\t\t}\n" +
				"\n" +
				"\t\t@Override\n" +
				"\t\tpublic Class<? extends PresenterFactory<?, ?>> getFactory()\n" +
				"\t\t{\n" +
				"\t\t\treturn " + field.getFactory() + ".class;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\t@Override\n" +
				"\t\tpublic String getPresenterId()\n" +
				"\t\t{\n" +
				"\t\t\treturn " + field.getPresenterId() + ";\n" +
				"\t\t}" +
				"\n" +
				"\t\t@Override\n" +
				"\t\tpublic Class<? extends ParamsHolder<?>> getParamsHolderClass()\n" +
				"\t\t{\n" +
				"\t\t\treturn " + field.getFactoryParamsHolder() + ".class;\n" +
				"\t\t}\n" +
				"\t}" +
				"\n";
		return builder + s;
	}

	private static class Field
	{
		private final DeclaredType mClazz;
		private final String mName;
		private final DeclaredType mFactory;
		private final String mFactoryParamsHolder;
		private final String mPresenterId;

		String mTag;
		PresenterType mType;

		public Field(final DeclaredType clazz, final String name, final String type, final String tag, DeclaredType factory, String presenterId)
		{
			mClazz = clazz;
			mName = name;
			mTag = tag;


			if (type == null)
			{
				mType = PresenterType.LOCAL;
			}
			else
			{
				mType = PresenterType.valueOf(type);
			}

			mFactory = factory;
			if (factory == null)
			{
				mFactoryParamsHolder = DefaultParamsHolder.class.getCanonicalName();
			}
			else
			{
				mFactoryParamsHolder = Util.getFullClassName(factory) + MvpProcessor.FACTORY_PARAMS_HOLDER_SUFFIX;
			}

			mPresenterId = presenterId;

		}

		public DeclaredType getClazz()
		{
			return mClazz;
		}

		public String getGeneratedClassName()
		{
			return mName + MvpProcessor.VIEW_STATE_CLASS_NAME_PROVIDER_SUFFIX;
		}

		public String getTag()
		{
			return mTag;
		}

		public String getName()
		{
			return mName;
		}

		public PresenterType getType()
		{
			return mType;
		}

		public String getFactory()
		{
			return mFactory != null ? mFactory.toString() : DefaultPresenterFactory.class.getCanonicalName();
		}

		public String getPresenterId()
		{
			return mPresenterId;
		}

		public String getFactoryParamsHolder()
		{
			return mFactoryParamsHolder;
		}
	}
}
