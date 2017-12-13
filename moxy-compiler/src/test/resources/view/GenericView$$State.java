package view;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;

public class GenericView$$State<T> extends MvpViewState<GenericView<T>> implements GenericView<T> {

	@Override
	public void testEvent(T param) {
		TestEventCommand testEventCommand = new TestEventCommand(param);
		mViewCommands.beforeApply(testEventCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (GenericView<T> view : mViews) {
			view.testEvent(param);
		}

		mViewCommands.afterApply(testEventCommand);
	}


	public class TestEventCommand extends ViewCommand<GenericView<T>> {
		public final T param;

		TestEventCommand(T param) {
			super("testEvent", AddToEndStrategy.class);
			this.param = param;
		}

		@Override
		public void apply(GenericView<T> mvpView) {
			mvpView.testEvent(param);
		}
	}
}