package com.omegar.mvp;

import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

import com.omegar.mvp.viewstate.MvpViewState;


import static java.lang.annotation.ElementType.TYPE;

/**
 * Inject view state to {@link MvpPresenter#mViews} and
 * {@link MvpPresenter#mViewState} presenter fields. Presenter, annotated with
 * this, should be strongly typed on view interface(not write some like extends
 * MvpPresenter&lt;V extends SuperView&gt;). Otherwise code generation make
 * code, that broke your app.
 */
@Target(value = TYPE)
@Inherited
public @interface InjectViewState {
	Class<? extends MvpViewState> value() default DefaultViewState.class;

	Class<? extends MvpView> view() default DefaultView.class;
}
