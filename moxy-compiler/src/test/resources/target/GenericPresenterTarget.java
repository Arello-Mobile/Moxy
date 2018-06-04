package target;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import presenter.GenericPresenter;
import view.GenericView;

public class GenericPresenterTarget implements GenericView<String> {

	@InjectPresenter
	GenericPresenter<String> mPresenter;

	@ProvidePresenter
	GenericPresenter<String> providePresenter() {
		return new GenericPresenter<>();
	}

	@Override
	public void testEvent(String param) {
	}
}