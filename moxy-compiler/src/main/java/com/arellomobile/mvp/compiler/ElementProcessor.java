package com.arellomobile.mvp.compiler;

import javax.lang.model.element.Element;

/**
 * Date: 27-Jul-17
 * Time: 10:31
 *
 * @author Evgeny Kursakov
 */
public abstract class ElementProcessor<E extends Element, R> {
	public abstract R process(E element);
}
