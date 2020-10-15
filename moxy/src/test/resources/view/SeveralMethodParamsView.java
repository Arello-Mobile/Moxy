package view;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.factory.MockPresenterFactory;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.InjectViewStatePresenter;
import com.omegar.mvp.presenter.PresenterType;

public class SeveralMethodParamsView implements MvpView, params.SeveralMethodParams {
	@InjectPresenter(factory = MockPresenterFactory.class, presenterId = "Test", type = PresenterType.LOCAL)
	public InjectViewStatePresenter mInjectViewStatePresenter;

	@Override
	public void method1() {

	}

	@Override
	public void method2() {

	}
}