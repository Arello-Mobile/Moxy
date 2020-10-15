package presenter;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.ViewStateProvider;
import com.omegar.mvp.viewstate.MvpViewState;

import view.GenericView$$State;

public class GenericPresenter$$ViewStateProvider extends ViewStateProvider {

	@Override
	public MvpViewState<? extends MvpView> getViewState() {
		return new GenericView$$State();
	}
}
