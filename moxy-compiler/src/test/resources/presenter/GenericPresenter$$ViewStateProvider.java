package presenter;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.ViewStateProvider;
import com.arellomobile.mvp.viewstate.MvpViewState;

import view.GenericView$$State;

public class GenericPresenter$$ViewStateProvider extends ViewStateProvider {

	@Override
	public MvpViewState<? extends MvpView> getViewState() {
		return new GenericView$$State();
	}
}
