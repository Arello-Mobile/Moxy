package presenter;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

/**
 * Date: 25.02.2016
 * Time: 13:36
 *
 * @author Savin Mikhail
 */
public class PresenterWithoutEmptyConstructor<V extends MvpView> extends MvpPresenter<V> {

	public PresenterWithoutEmptyConstructor(String s) {/*do nothing*/}

	private PresenterWithoutEmptyConstructor() {/*do nothing*/}
}
