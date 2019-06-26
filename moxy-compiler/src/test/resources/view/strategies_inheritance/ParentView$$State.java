package view.strategies_inheritance;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import java.lang.Override;
import java.lang.String;
import view.strategies_inheritance.strategies.ParentDefaultStrategy;
import view.strategies_inheritance.strategies.Strategy1;

public class ParentView$$State extends MvpViewState<ParentView> implements ParentView {
	@Override
	public void parentMethod1() {
		ParentMethod1Command parentMethod1Command = new ParentMethod1Command();
		mViewCommands.beforeApply(parentMethod1Command);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (ParentView view : mViews) {
			view.parentMethod1();
		}

		mViewCommands.afterApply(parentMethod1Command);
	}

	@Override
	public void parentMethod2() {
		ParentMethod2Command parentMethod2Command = new ParentMethod2Command();
		mViewCommands.beforeApply(parentMethod2Command);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (ParentView view : mViews) {
			view.parentMethod2();
		}

		mViewCommands.afterApply(parentMethod2Command);
	}

	@Override
	public void parentMethod3() {
		ParentMethod3Command parentMethod3Command = new ParentMethod3Command();
		mViewCommands.beforeApply(parentMethod3Command);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (ParentView view : mViews) {
			view.parentMethod3();
		}

		mViewCommands.afterApply(parentMethod3Command);
	}

	@Override
	public void parentMethodWithArg(String i) {
		ParentMethodWithArgCommand parentMethodWithArgCommand = new ParentMethodWithArgCommand(i);
		mViewCommands.beforeApply(parentMethodWithArgCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (ParentView view : mViews) {
			view.parentMethodWithArg(i);
		}

		mViewCommands.afterApply(parentMethodWithArgCommand);
	}

	@Override
	public void parentMethodWithArg2(String i) {
		ParentMethodWithArg2Command parentMethodWithArg2Command = new ParentMethodWithArg2Command(i);
		mViewCommands.beforeApply(parentMethodWithArg2Command);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (ParentView view : mViews) {
			view.parentMethodWithArg2(i);
		}

		mViewCommands.afterApply(parentMethodWithArg2Command);
	}

	@Override
	public void parentMethodWithArg3(String i) {
		ParentMethodWithArg3Command parentMethodWithArg3Command = new ParentMethodWithArg3Command(i);
		mViewCommands.beforeApply(parentMethodWithArg3Command);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (ParentView view : mViews) {
			view.parentMethodWithArg3(i);
		}

		mViewCommands.afterApply(parentMethodWithArg3Command);
	}

	@Override
	public void parentMethodWithStrategy() {
		ParentMethodWithStrategyCommand parentMethodWithStrategyCommand = new ParentMethodWithStrategyCommand();
		mViewCommands.beforeApply(parentMethodWithStrategyCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for (ParentView view : mViews) {
			view.parentMethodWithStrategy();
		}

		mViewCommands.afterApply(parentMethodWithStrategyCommand);
	}

	public class ParentMethod1Command extends ViewCommand<ParentView> {
		ParentMethod1Command() {
			super("parentMethod1", ParentDefaultStrategy.class);
		}

		@Override
		public void apply(ParentView mvpView) {
			mvpView.parentMethod1();
		}
	}

	public class ParentMethod2Command extends ViewCommand<ParentView> {
		ParentMethod2Command() {
			super("parentMethod2", ParentDefaultStrategy.class);
		}

		@Override
		public void apply(ParentView mvpView) {
			mvpView.parentMethod2();
		}
	}

	public class ParentMethod3Command extends ViewCommand<ParentView> {
		ParentMethod3Command() {
			super("parentMethod3", ParentDefaultStrategy.class);
		}

		@Override
		public void apply(ParentView mvpView) {
			mvpView.parentMethod3();
		}
	}

	public class ParentMethodWithArgCommand extends ViewCommand<ParentView> {
		public final String i;

		ParentMethodWithArgCommand(String i) {
			super("parentMethodWithArg", ParentDefaultStrategy.class);

			this.i = i;
		}

		@Override
		public void apply(ParentView mvpView) {
			mvpView.parentMethodWithArg(i);
		}
	}

	public class ParentMethodWithArg2Command extends ViewCommand<ParentView> {
		public final String i;

		ParentMethodWithArg2Command(String i) {
			super("parentMethodWithArg2", ParentDefaultStrategy.class);

			this.i = i;
		}

		@Override
		public void apply(ParentView mvpView) {
			mvpView.parentMethodWithArg2(i);
		}
	}

	public class ParentMethodWithArg3Command extends ViewCommand<ParentView> {
		public final String i;

		ParentMethodWithArg3Command(String i) {
			super("parentMethodWithArg3", ParentDefaultStrategy.class);

			this.i = i;
		}

		@Override
		public void apply(ParentView mvpView) {
			mvpView.parentMethodWithArg3(i);
		}
	}

	public class ParentMethodWithStrategyCommand extends ViewCommand<ParentView> {
		ParentMethodWithStrategyCommand() {
			super("parentMethodWithStrategy", Strategy1.class);
		}

		@Override
		public void apply(ParentView mvpView) {
			mvpView.parentMethodWithStrategy();
		}
	}
}
