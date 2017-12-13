package presenter;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.ViewStateProvider;
import com.arellomobile.mvp.viewstate.MvpViewState;

import view.EmptyView$$State;

public class EmptyViewPresenter$$ViewStateProvider extends ViewStateProvider {

	@Override
	public MvpViewState<? extends MvpView> getViewState() {
		return new EmptyView$$State();
	}
}
