package target;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import presenter.EmptyViewPresenter;
import view.EmptyView;

public class SimpleProvidePresenterTarget implements EmptyView {
	@InjectPresenter
	EmptyViewPresenter presenter;

	@ProvidePresenter
	EmptyViewPresenter providePresenter() {
		return new EmptyViewPresenter();
	}
}
