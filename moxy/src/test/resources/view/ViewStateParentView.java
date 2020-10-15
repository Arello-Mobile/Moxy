package view;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 26.02.2016
 * Time: 12:08
 *
 * @author Savin Mikhail
 */

public interface ViewStateParentView extends MvpView {
	@StateStrategyType(value = AddToEndSingleStrategy.class)
	void method1();

	@StateStrategyType(value = AddToEndSingleStrategy.class, tag = "Test")
	void method2();
}
