package multimodules.app;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.ViewStateProvider;
import com.omegar.mvp.viewstate.MvpViewState;

public class AppPresenter$$ViewStateProvider extends ViewStateProvider {
	@Override
	public MvpViewState<? extends MvpView> getViewState() {
		return new AppView$$State();
	}
}
