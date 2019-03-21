package com.omegar.mvp.presenter;

import com.omegar.mvp.MvpPresenter;
import com.omegar.mvp.view.TestView;

/**
 * Date: 10.02.2016
 * Time: 13:24
 *
 * @author Savin Mikhail
 */
public class TestViewPresenter extends MvpPresenter<TestView> {
	public void testEvent() {
		getViewState().testEvent();
	}
}
