package view;

import com.omegar.mvp.MvpView;
import com.arellomobile.mvp.factory.MockPresenterFactory;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.PresenterType;

import params.Params1;
import presenter.PositiveParamsViewPresenter;

/**
 * Date: 25.02.2016
 * Time: 11:19
 *
 * @author Savin Mikhail
 */
public class PositiveParamsView implements MvpView, Params1 {
	@InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
	public PositiveParamsViewPresenter<PositiveParamsView> mInjectViewStatePresenter;

	@Override
	public String mockParams1(final String presenterId) {
		return null;
	}
}