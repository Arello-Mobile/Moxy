package view;

import com.arellomobile.mvp.factory.MockPresenterFactory2;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.TestViewPresenter;
import com.arellomobile.mvp.view.TestView;

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
