package view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.TestViewPresenter;

/**
 * Date: 25.02.2016
 * Time: 12:50
 *
 * @author Savin Mikhail
 */
public class InjectPresenterWithIncorrectViewView implements MvpView {
	@InjectPresenter
	public TestViewPresenter mTestViewPresenter;
}
