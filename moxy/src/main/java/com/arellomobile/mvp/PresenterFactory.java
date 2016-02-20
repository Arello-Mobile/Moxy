package com.arellomobile.mvp;

/**
 * Date: 21-Dec-15
 * Time: 14:15
 *
 * @author Alexander Blinov
 */

/**
 * PresenterFactory for providing presenters
 * <p>
 * You can getPresenterFactory access to presenters from hear using listeners and manipulate them directly
 */
public interface PresenterFactory<Presenter extends MvpPresenter<?>, Params>
{
	/**
	 * This method creates presenter Instance.
	 *
	 * @param defaultInstance instance created by empty constructor, or null if empty constructor not exists
	 * @param presenterClazz expected clazz of presenter
	 * @param params         initial params for presenter.
	 * @return MvpPresenter instance.
	 */
	Presenter createPresenter(Presenter defaultInstance, Class<Presenter> presenterClazz, Params params);

	/**
	 * @param presenterClazz expected clazz of presenter
	 * @param params         initial params for presenter.
	 * @return unique tag for presenter created via {@link #createPresenter(MvpPresenter, Class, Object)}
	 */
	String createTag(Class<Presenter> presenterClazz, Params params);
}
