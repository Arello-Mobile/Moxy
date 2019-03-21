package com.omegar.mvp.view;

import com.omegar.mvp.GenerateViewState;
import com.omegar.mvp.MvpView;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 29.02.2016
 * Time: 9:09
 *
 * @author Savin Mikhail
 */
@GenerateViewState
public interface ParentView extends MvpView {
	void withoutStrategyMethod();

	@StateStrategyType(AddToEndSingleStrategy.class)
	void customStrategyMethod();

	@StateStrategyType(AddToEndSingleStrategy.class)
	void parentOverrideMethodWithCustomStrategy();
}
