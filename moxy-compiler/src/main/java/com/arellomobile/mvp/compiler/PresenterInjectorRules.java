package com.arellomobile.mvp.compiler;

import com.arellomobile.mvp.presenter.InjectPresenter;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

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
		if (annotatedField.getKind() != mValidKind)
		{
			mErrorBuilder.append(annotatedField + " must be " + mValidKind.name() + ", or not mark it as @" + InjectPresenter.class.getSimpleName()).append("\n");
		}

		for (Modifier modifier : annotatedField.getModifiers())
		{
			if (!mValidModifiers.contains(modifier))
			{
				mErrorBuilder.append(annotatedField + " can't be a " + modifier).append(". Use ").append(validModifiersToString()).append("\n");
			}
		}
	}
}
