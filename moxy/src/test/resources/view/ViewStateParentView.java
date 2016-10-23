package view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

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
