package view;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;

import java.io.Serializable;

public class GenericWithExtendsView$$State<T extends Serializable> extends MvpViewState<GenericWithExtendsView<T>> implements GenericWithExtendsView<T> {

	@Override
	public void testEvent(T param) {
		TestEventCommand testEventCommand = new TestEventCommand(param);
		mViewCommands.beforeApply(testEventCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (GenericWithExtendsView<T> view : mViews) {
			view.testEvent(param);
		}

		mViewCommands.afterApply(testEventCommand);
	}


	public class TestEventCommand extends ViewCommand<GenericWithExtendsView<T>> {
		public final T param;

		TestEventCommand(T param) {
			super("testEvent", AddToEndStrategy.class);
			this.param = param;
		}

		@Override
		public void apply(GenericWithExtendsView<T> mvpView) {
			mvpView.testEvent(param);
		}
	}
}