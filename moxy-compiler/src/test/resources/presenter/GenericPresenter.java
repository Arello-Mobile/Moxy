package presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import view.GenericView;

@InjectViewState
public class GenericPresenter<T> extends MvpPresenter<GenericView<T>> {
}