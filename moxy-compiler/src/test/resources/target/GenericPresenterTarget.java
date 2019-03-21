package target;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import presenter.GenericPresenter;
import view.GenericView;

public class GenericPresenterTarget implements GenericView<String> {

	@InjectPresenter
	GenericPresenter<String> presenter;

	@ProvidePresenter
	GenericPresenter<String> providePresenter() {
		return new GenericPresenter<>();
	}

	@Override
	public void testEvent(String param) {
	}
}