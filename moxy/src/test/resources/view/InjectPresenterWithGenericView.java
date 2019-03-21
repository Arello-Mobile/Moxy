package view;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.view.CounterTestView;

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
