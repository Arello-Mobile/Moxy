package presenter;

import com.omegar.mvp.MvpPresenter;
import com.omegar.mvp.view.CounterTestView;
import com.omegar.mvp.view.TestView;

/**
 * Date: 04.03.2016
 * Time: 11:27
 *
 * @author Savin Mikhail
 */
public class WithViewGenericPresenter2<T extends TestView, S extends CounterTestView> extends MvpPresenter<T> {
}
