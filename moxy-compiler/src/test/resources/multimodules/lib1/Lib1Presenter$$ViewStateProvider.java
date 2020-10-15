package multimodules.lib1;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.ViewStateProvider;
import com.omegar.mvp.viewstate.MvpViewState;

public class Lib1Presenter$$ViewStateProvider extends ViewStateProvider {
	@Override
	public MvpViewState<? extends MvpView> getViewState() {
		return new Lib1View$$State();
	}
}
