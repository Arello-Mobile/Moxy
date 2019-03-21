package view;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 26.02.2016
 * Time: 12:13
 *
 * @author Savin Mikhail
 */
public interface ViewStateParentStrategyTagView {
	@StateStrategyType(AddToEndSingleStrategy.class)
	void method1();

	@StateStrategyType(value = AddToEndSingleStrategy.class, tag = "Test2")
	void method2();
}
