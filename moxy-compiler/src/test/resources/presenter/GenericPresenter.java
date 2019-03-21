package presenter;

import com.omegar.mvp.InjectViewState;
import com.omegar.mvp.MvpPresenter;

import view.GenericView;

@InjectViewState
public class GenericPresenter<T> extends MvpPresenter<GenericView<T>> {
}