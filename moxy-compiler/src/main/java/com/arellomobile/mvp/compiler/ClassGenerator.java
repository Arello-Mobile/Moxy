package com.arellomobile.mvp.compiler;

import java.util.List;

/**
 * Date: 12-Jan-16
 * Time: 10:35
 *
 * @author Alexander Blinov
 */
abstract class ClassGenerator<ElementType>
{
	abstract boolean generate(ElementType element, List<ClassGeneratingParams> classGeneratingParamsList);

	@SuppressWarnings("unused")
	protected static String print(String builder, final String string)
	{
		builder += "\n" +
				"//" + string +
				"\n";
		return builder;
	}
}
