package view;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.SingleStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 26.02.2016
 * Time: 12:13
 *
 * @author Savin Mikhail
 */
public interface ViewStateParentStrategyClassView {
	@StateStrategyType(SingleStateStrategy.class)
	void method1();

	@StateStrategyType(value = AddToEndSingleStrategy.class, tag = "Test")
	void method2();
}
