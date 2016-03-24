package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.arellomobile.mvp.compiler.Util.fillGenerics;

/**
 * Date: 17-Feb-16
 * Time: 17:00
 *
 * @author esorokin
 */
public class PresenterInjectorRules extends AnnotationRule
{

	public PresenterInjectorRules(ElementKind validKind, Modifier... validModifiers)
	{
		super(validKind, validModifiers);
	}

	@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
	@Override
	public void checkAnnotation(Element annotatedField)
	{
		checkEnvironment(annotatedField);

		if (annotatedField.getKind() != mValidKind)
		{
			mErrorBuilder.append("Field " + annotatedField + " of " + annotatedField.getEnclosingElement().getSimpleName() + " should be " + mValidKind.name() + ", or not mark it as @" + InjectPresenter.class.getSimpleName()).append("\n");
		}

		for (Modifier modifier : annotatedField.getModifiers())
		{
			if (!mValidModifiers.contains(modifier))
			{
				mErrorBuilder.append("Field " + annotatedField + " of " + annotatedField.getEnclosingElement().getSimpleName() + " can't be a " + modifier).append(". Use ").append(validModifiersToString()).append("\n");
			}
		}
	}

	private void checkEnvironment(final Element annotatedField)
	{
		if (!(annotatedField.asType() instanceof DeclaredType))
		{
			return;
		}

		TypeElement typeElement = (TypeElement) ((DeclaredType) annotatedField.asType()).asElement();
		String viewClassFromGeneric = getViewClassFromGeneric(typeElement);
		List<TypeMirror> viewsType = getViewsType((TypeElement) ((DeclaredType) annotatedField.getEnclosingElement().asType()).asElement());
		boolean result = false;
		for (TypeMirror typeMirror : viewsType)
		{
			if (Util.getFullClassName(typeMirror).equals(viewClassFromGeneric))
			{
				result = true;
				break;
			}
		}
		if (!result)
		{
			MvpCompiler.getMessager().printMessage(Diagnostic.Kind.ERROR, "You can't use @InjectPresenter for class which not extends from presenter view.", annotatedField);
		}
	}

	private String getViewClassFromGeneric(TypeElement typeElement)
	{
		TypeMirror superclass = typeElement.asType();

		Map<String, String> parentTypes = Collections.emptyMap();

		if (!typeElement.getTypeParameters().isEmpty())
		{
			//TODO change to correct text;
			MvpCompiler.getMessager().printMessage(Diagnostic.Kind.ERROR, "Your " + typeElement.getSimpleName() + " is typed. @InjectPresenter can't be used for typed presenter.");
		}

		while (superclass.getKind() != TypeKind.NONE)
		{
			TypeElement superclassElement = (TypeElement) ((DeclaredType) superclass).asElement();

			final List<? extends TypeMirror> typeArguments = ((DeclaredType) superclass).getTypeArguments();
			final List<? extends TypeParameterElement> typeParameters = superclassElement.getTypeParameters();

			Map<String, String> types = new HashMap<>();
			for (int i = 0; i < typeArguments.size(); i++)
			{
				types.put(typeParameters.get(i).toString(), fillGenerics(parentTypes, typeArguments.get(i)));
			}

			if (superclassElement.toString().equals(MvpPresenter.class.getCanonicalName()))
			{
				// MvpPresenter is typed only on View class
				return fillGenerics(parentTypes, typeArguments);
			}

			parentTypes = types;

			superclass = superclassElement.getSuperclass();
		}

		return "";
	}

	private List<TypeMirror> getViewsType(TypeElement typeElement)
	{
		TypeMirror superclass = typeElement.asType();

		List<TypeMirror> result = new ArrayList<>();

		if (!typeElement.getTypeParameters().isEmpty())
		{
			MvpCompiler.getMessager().printMessage(Diagnostic.Kind.ERROR, "Your " + typeElement.getSimpleName() + " is typed. @InjectPresenter can't be used for typed presenter.");
		}

		while (superclass.getKind() != TypeKind.NONE)
		{
			TypeElement superclassElement = (TypeElement) ((DeclaredType) superclass).asElement();
			result.addAll(superclassElement.getInterfaces());
			result.add(superclass);

			superclass = superclassElement.getSuperclass();
		}

		return result;
	}
}
