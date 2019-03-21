package view;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.presenter.InjectPresenter;

/**
 * Date: 25.02.2016
 * Time: 11:39
 *
 * @author Savin Mikhail
 */
public class ObjectInjectPresenterView implements MvpView {
	@InjectPresenter
	public Object mObject;
}
