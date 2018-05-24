package view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface StrategiesView extends MvpView {
	@StateStrategyType(SingleStateStrategy.class)
	void singleState();

	@StateStrategyType(OneExecutionStateStrategy.class)
	void oneExecution();

	void withoutStrategy();
}