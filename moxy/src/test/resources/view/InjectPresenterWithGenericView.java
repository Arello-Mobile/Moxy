package view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.view.CounterTestView;
import com.arellomobile.mvp.view.TestView;
import com.arellomobile.mvp.view.TestViewChild2;

import presenter.WithViewGenericPresenter;

/**
 * Date: 04.03.2016
 * Time: 11:27
 *
 * @author Savin Mikhail
 */
public class InjectPresenterWithGenericView extends CounterTestView {
	@InjectPresenter
	WithViewGenericPresenter<InjectPresenterWithGenericView, CounterTestView> mPresenter;

	public void testEvent() {

	}
}
