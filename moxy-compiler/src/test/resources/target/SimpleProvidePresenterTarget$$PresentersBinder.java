package target;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.PresenterBinder;
import com.arellomobile.mvp.presenter.PresenterField;
import com.arellomobile.mvp.presenter.PresenterType;

import java.util.ArrayList;
import java.util.List;

import presenter.EmptyViewPresenter;

public class SimpleProvidePresenterTarget$$PresentersBinder extends PresenterBinder<SimpleProvidePresenterTarget> {
	public List<PresenterField<SimpleProvidePresenterTarget>> getPresenterFields() {
		List<PresenterField<SimpleProvidePresenterTarget>> presenters = new ArrayList<>(1);

		presenters.add(new presenterBinder());

		return presenters;
	}

	public class presenterBinder extends PresenterField<SimpleProvidePresenterTarget> {
		public presenterBinder() {
			super("presenter", PresenterType.LOCAL, null, EmptyViewPresenter.class);
		}

		@Override
		public void bind(SimpleProvidePresenterTarget target, MvpPresenter presenter) {
			target.presenter = (EmptyViewPresenter) presenter;
		}

		@Override
		public MvpPresenter<?> providePresenter(SimpleProvidePresenterTarget delegated) {
			return delegated.providePresenter();
		}
	}

}