package view.strategies_inheritance;

import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import view.strategies_inheritance.strategies.ChildDefaultStrategy;
import view.strategies_inheritance.strategies.Strategy2;

import javax.annotation.Nullable;

@StateStrategyType(ChildDefaultStrategy.class)
public interface ChildView extends ParentView {
	void parentMethod1(); // ParentDefaultStrategy -> ChildDefaultStrategy

	@StateStrategyType(Strategy2.class)
	void parentMethod2(); // ParentDefaultStrategy -> Strategy2

	void childMethod(); // ChildDefaultStrategy

	@Override
    void parentMethodWithArg(final String i); // ParentDefaultStrategy -> ChildDefaultStrategy

	void parentMethodWithArg2(@Nullable String i); // ParentDefaultStrategy

	@StateStrategyType(Strategy2.class)
	void childMethodWithStrategy(); // Strategy2
}