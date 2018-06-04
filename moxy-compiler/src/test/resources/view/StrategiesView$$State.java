package view;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;

public class StrategiesView$$State extends MvpViewState<StrategiesView> implements StrategiesView {

	@Override
	public void singleState() {
		SingleStateCommand singleStateCommand = new SingleStateCommand();
		mViewCommands.beforeApply(singleStateCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (StrategiesView view : mViews) {
			view.singleState();
		}

		mViewCommands.afterApply(singleStateCommand);
	}

	@Override
	public void oneExecution() {
		OneExecutionCommand oneExecutionCommand = new OneExecutionCommand();
		mViewCommands.beforeApply(oneExecutionCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (StrategiesView view : mViews) {
			view.oneExecution();
		}

		mViewCommands.afterApply(oneExecutionCommand);
	}

	@Override
	public void withoutStrategy() {
		WithoutStrategyCommand withoutStrategyCommand = new WithoutStrategyCommand();
		mViewCommands.beforeApply(withoutStrategyCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (StrategiesView view : mViews) {
			view.withoutStrategy();
		}

		mViewCommands.afterApply(withoutStrategyCommand);
	}


	public class SingleStateCommand extends ViewCommand<StrategiesView> {
		SingleStateCommand() {
			super("singleState", SingleStateStrategy.class);
		}

		@Override
		public void apply(StrategiesView mvpView) {
			mvpView.singleState();
		}
	}

	public class OneExecutionCommand extends ViewCommand<StrategiesView> {
		OneExecutionCommand() {
			super("oneExecution", OneExecutionStateStrategy.class);
		}

		@Override
		public void apply(StrategiesView mvpView) {
			mvpView.oneExecution();
		}
	}

	public class WithoutStrategyCommand extends ViewCommand<StrategiesView> {
		WithoutStrategyCommand() {
			super("withoutStrategy", AddToEndSingleStrategy.class);
		}

		@Override
		public void apply(StrategiesView mvpView) {
			mvpView.withoutStrategy();
		}
	}
}