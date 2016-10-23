package presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.view.CounterTestView;

/**
 * Date: 15.03.2016
 * Time: 13:32
 *
 * @author Savin Mikhail
 */
@InjectViewState
public class InjectViewStateForGenericPresenter<T extends CounterTestView> extends MvpPresenter<T> {
}
