package view;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.presenter.InjectPresenter;

/**
 * Date: 25.02.2016
 * Time: 11:39
 *
 * @author Savin Mikhail
 */
public class InjectPresenterAnnotationInsideNotMvpViewClass {
	@InjectPresenter
	public MvpPresenter<MvpView> mObject;
}
