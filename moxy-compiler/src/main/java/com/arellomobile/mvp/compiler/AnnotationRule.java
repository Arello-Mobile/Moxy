package com.arellomobile.mvp.compiler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

/**
 * Date: 17-Feb-16
 * Time: 16:57
 *
 * @author esorokin
 */
public abstract class AnnotationRule {
	protected final ElementKind mValidKind;
	protected final Set<Modifier> mValidModifiers;
	protected StringBuilder mErrorBuilder;

	public AnnotationRule(ElementKind validKind, Modifier... validModifiers) {
		if (validModifiers == null || validModifiers.length == 0) {
			throw new RuntimeException("Valid modifiers cant be empty or null.");
		}

		mValidKind = validKind;
		mValidModifiers = new HashSet<>(Arrays.asList(validModifiers));
		mErrorBuilder = new StringBuilder();
	}

	/**
	 * Method describe rules for using Annotation.
	 *
	 * @param AnnotatedField Checking annotated field.
	 */
	public abstract void checkAnnotation(Element AnnotatedField);

	public String getErrorStack() {
		return mErrorBuilder.toString();
	}

	protected String validModifiersToString() {
		if (mValidModifiers.size() > 1) {
			StringBuilder result = new StringBuilder("one of [");
			boolean addSeparator = false;
			for (Modifier validModifier : mValidModifiers) {
				if (addSeparator) {
					result.append(", ");
				}
				addSeparator = true;
				result.append(validModifier.toString());
			}
			result.append("]");
			return result.toString();
		} else {
			return mValidModifiers.iterator().next() + ".";
		}
	}
}
