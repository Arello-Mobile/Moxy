package view;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.presenter.InjectPresenter;

import presenter.PresenterWithoutEmptyConstructor;

/**
 * Date: 10.02.2016
 * Time: 13:22
 *
 * @author Savin Mikhail
 */
public class InjectPresenterWithoutEmptyConstructorView implements MvpView {

	@InjectPresenter
	public PresenterWithoutEmptyConstructor<InjectPresenterWithoutEmptyConstructorView> mPresenterWithoutEmptyConstructorViewPresenterWithoutEmptyConstructor;
}
