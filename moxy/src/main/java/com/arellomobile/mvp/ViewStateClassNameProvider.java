package com.arellomobile.mvp;

/**
 * Date: 18.12.2015
 * Time: 13:15
 *
 * @author Yuri Shmakov
 */
public class ViewStateClassNameProvider
{
	private final String viewStateClassName;

	public ViewStateClassNameProvider(String viewStateClassName)
	{
		this.viewStateClassName = viewStateClassName;
	}

	/**
	 * <p>Presenter creates view state object by class name from this method.</p>
	 *
	 * @return view state class name
	 */
	public String getViewStateClassName()
	{
		return viewStateClassName;
	}
}
