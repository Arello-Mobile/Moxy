package view;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.SingleStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface StrategiesView extends MvpView {
	@StateStrategyType(SingleStateStrategy.class)
	void singleState();

	@StateStrategyType(OneExecutionStateStrategy.class)
	void oneExecution();

	void withoutStrategy();
}