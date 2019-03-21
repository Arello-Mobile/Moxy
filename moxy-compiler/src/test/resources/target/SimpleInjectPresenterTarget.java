package target;

import com.omegar.mvp.presenter.InjectPresenter;

import presenter.EmptyViewPresenter;
import view.EmptyView;

public class SimpleInjectPresenterTarget implements EmptyView {
	@InjectPresenter
	EmptyViewPresenter presenter;
}
