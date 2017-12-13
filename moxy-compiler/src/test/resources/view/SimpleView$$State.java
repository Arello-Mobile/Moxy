package view;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;

public class SimpleView$$State extends MvpViewState<SimpleView> implements SimpleView {

	@Override
	public void testEvent() {
		TestEventCommand testEventCommand = new TestEventCommand();
		mViewCommands.beforeApply(testEventCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (SimpleView view : mViews) {
			view.testEvent();
		}

		mViewCommands.afterApply(testEventCommand);
	}


	public class TestEventCommand extends ViewCommand<SimpleView> {
		TestEventCommand() {
			super("testEvent", AddToEndStrategy.class);
		}

		@Override
		public void apply(SimpleView mvpView) {
			mvpView.testEvent();
		}
	}
}