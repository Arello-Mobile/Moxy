package com.arellomobile.mvp;

/**
 * Date: 18.12.2015
 * Time: 13:15
 *
 * @author Yuri Shmakov
 */
public interface ViewStateClassNameProvider
{
	/**
	 * <p>Presenter creates view state object by class name from this method.</p>
	 *
	 * @return view state class name
	 */
	String getViewStateClassName();
}
