package com.arellomobile.mvp.compiler;

import java.util.List;

import com.arellomobile.mvp.DefaultPresenterFactory;
import com.arellomobile.mvp.MvpProcessor;
import com.arellomobile.mvp.ParamsProvider;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

/**
 * Date: 12-Jan-16
 * Time: 10:52
 *
 * @author Alexander Blinov
 */
final class ParamsHolderClassGenerator extends ClassGenerator<TypeElement>
{
	@Override
	public boolean generate(TypeElement typeElement, ClassGeneratingParams classGeneratingParams)
	{
		ParamsProvider annotation = typeElement.getAnnotation(ParamsProvider.class);

		if (containsManyMethods(typeElement))
		{
			throwError();
		}

		for (Element element : typeElement.getEnclosedElements())
		{
			if (!(element instanceof ExecutableElement))
			{
				continue;
			}

			final ExecutableElement methodElement = (ExecutableElement) element;

			String methodName = methodElement.getSimpleName().toString();

			String returnType = methodElement.getReturnType().toString();

			if (!parametersValid(methodElement.getParameters()))
			{
				throwError();
			}


			String defaultFactoryName = DefaultPresenterFactory.class.getCanonicalName();
			String parentClassName = defaultFactoryName.substring(defaultFactoryName.lastIndexOf(".") + 1);
			if (annotation != null)
			{
				TypeMirror value;
				try
				{
					annotation.value();
				}
				catch (MirroredTypeException mte)
				{
					value = mte.getTypeMirror();
					parentClassName = value.toString();
				}
			}

			classGeneratingParams.setName(parentClassName + MvpProcessor.FACTORY_PARAMS_HOLDER_SUFFIX);

			final String className = typeElement.getQualifiedName().toString();
			final String viewClassName = parentClassName.substring(parentClassName.lastIndexOf(".") + 1);

			String builder = "package " + parentClassName.substring(0, parentClassName.lastIndexOf(".")) + ";\n" +
					"\n" +
					"import com.arellomobile.mvp.ParamsHolder;\n" +
					"import com.arellomobile.mvp.presenter.PresenterField;\n" +
					"\n" +
					"public class " + viewClassName + MvpProcessor.FACTORY_PARAMS_HOLDER_SUFFIX + " implements ParamsHolder<" + returnType + ">" +//" implements PresenterBinder<" + parentClassName + ">" +
					"\n" +
					"{\n" +
					"\t@Override\n" +
					"\tpublic " + returnType + " getParams(PresenterField<?> presenterField, Object delegated, String delegateTag)\n" +
					"\t{\n" +
					"\t\treturn ((" + className + ") delegated)." + methodName + "(presenterField.getPresenterId());\n" +
					"\t}\n" +
					"}\n";


			classGeneratingParams.setBody(builder);
			return true;
		}

		return false;
	}

	private boolean containsManyMethods(TypeElement typeElement)
	{
		boolean hasExecutable = false;

		for (Element element : typeElement.getEnclosedElements())
		{
			if ((element instanceof ExecutableElement))
			{
				if (hasExecutable)
				{
					return true;
				}
				else
				{
					hasExecutable = true;
				}
			}
		}
		return false;
	}

	private void throwError()
	{
		throw new RuntimeException("Your params provider interface should contains only one methods, annotated as @ParamsProvider, " +
				"this method should have only one String argument(that will be received presenter id)");
	}

	private boolean parametersValid(List<? extends VariableElement> parameters)
	{
		if (parameters == null || parameters.size() != 1)
		{
			return false;
		}

		//noinspection RedundantIfStatement
		if (parameters.get(0).asType().toString().equals(String.class.getName()))
		{
			return true;
		}
		return false;
	}

}
