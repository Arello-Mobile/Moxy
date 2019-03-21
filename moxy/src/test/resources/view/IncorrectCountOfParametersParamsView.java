package view;

import com.omegar.mvp.MvpView;
import com.arellomobile.mvp.factory.MockPresenterFactory;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.InjectViewStatePresenter;
import com.omegar.mvp.presenter.PresenterType;

import params.IncorrectCountOfParametersParams;

/**
 * Date: 24.02.2016
 * Time: 18:19
 *
 * @author Savin Mikhail
 */
public class IncorrectCountOfParametersParamsView implements MvpView, IncorrectCountOfParametersParams {
	@InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
	public InjectViewStatePresenter mInjectViewStatePresenter;

	@Override
	public void method1(final String s1, String s2) {

	}
}
