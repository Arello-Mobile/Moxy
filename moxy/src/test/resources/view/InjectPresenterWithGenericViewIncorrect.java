package view;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.view.CounterTestView;
import com.omegar.mvp.view.TestViewChild2;

import presenter.WithViewGenericPresenter;

/**
 * Date: 04.03.2016
 * Time: 11:27
 *
 * @author Savin Mikhail
 */
public class InjectPresenterWithGenericViewIncorrect extends TestViewChild2<MvpView> {
	@InjectPresenter
	WithViewGenericPresenter<CounterTestView, InjectPresenterWithGenericViewIncorrect> mPresenter;

	public void testEvent() {

	}
}
