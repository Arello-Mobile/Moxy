package view;

import com.omegar.mvp.factory.MockPresenterFactory2;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.PresenterType;
import com.omegar.mvp.presenter.TestViewPresenter;
import com.omegar.mvp.view.TestView;

import params.Params1;

/**
 * Date: 25.02.2016
 * Time: 13:48
 *
 * @author Savin Mikhail
 */
public class InjectPresenterWithIncorrectParamsView implements TestView, Params1 {
	@InjectPresenter(factory = MockPresenterFactory2.class, presenterId = "Test", type = PresenterType.LOCAL)
	public TestViewPresenter mPresenter;

	@Override
	public void testEvent() {

	}

	@Override
	public String mockParams1(final String presenterId) {
		return null;
	}
}
