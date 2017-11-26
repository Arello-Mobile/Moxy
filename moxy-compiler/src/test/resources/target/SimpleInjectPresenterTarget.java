package target;

import com.arellomobile.mvp.presenter.InjectPresenter;

import presenter.EmptyViewPresenter;
import view.EmptyView;

public class SimpleInjectPresenterTarget implements EmptyView {
	@InjectPresenter
	EmptyViewPresenter presenter;
}
