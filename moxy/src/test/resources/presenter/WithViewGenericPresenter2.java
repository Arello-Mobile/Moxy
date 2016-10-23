package presenter;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.view.CounterTestView;
import com.arellomobile.mvp.view.TestView;
import com.arellomobile.mvp.view.TestViewChild;

import view.PositiveParamsView;

/**
 * Date: 04.03.2016
 * Time: 11:27
 *
 * @author Savin Mikhail
 */
public class WithViewGenericPresenter2<T extends TestView, S extends CounterTestView> extends MvpPresenter<T> {
}
