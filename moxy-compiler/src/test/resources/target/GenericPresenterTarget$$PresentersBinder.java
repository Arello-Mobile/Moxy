package target;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.PresenterBinder;
import com.arellomobile.mvp.presenter.PresenterField;
import com.arellomobile.mvp.presenter.PresenterType;

import java.util.ArrayList;
import java.util.List;

import presenter.GenericPresenter;

public class GenericPresenterTarget$$PresentersBinder extends PresenterBinder<GenericPresenterTarget> {
	public List<PresenterField<GenericPresenterTarget>> getPresenterFields() {
		List<PresenterField<GenericPresenterTarget>> presenters = new ArrayList<>();

		presenters.add(new presenterBinder());

		return presenters;
	}

	public class presenterBinder extends PresenterField<GenericPresenterTarget> {
		public presenterBinder() {
			super("presenter", PresenterType.LOCAL, null, GenericPresenter.class);
		}

		@Override
		public void bind(GenericPresenterTarget target, MvpPresenter presenter) {
			target.presenter = (GenericPresenter) presenter;
		}

		@Override
		public MvpPresenter<?> providePresenter(GenericPresenterTarget delegated) {
			return delegated.providePresenter();
		}
	}

}