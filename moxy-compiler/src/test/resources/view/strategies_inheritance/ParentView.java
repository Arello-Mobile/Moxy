package view.strategies_inheritance;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import view.strategies_inheritance.strategies.ParentDefaultStrategy;
import view.strategies_inheritance.strategies.Strategy1;

@StateStrategyType(ParentDefaultStrategy.class)
public interface ParentView extends MvpView {
	void parentMethod1(); // ParentDefaultStrategy

	void parentMethod2(); // ParentDefaultStrategy

	void parentMethod3(); // ParentDefaultStrategy

	@StateStrategyType(Strategy1.class)
	void parentMethodWithStrategy(); // Strategy1
}