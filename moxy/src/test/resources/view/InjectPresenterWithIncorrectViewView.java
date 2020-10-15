package view;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.TestViewPresenter;

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
