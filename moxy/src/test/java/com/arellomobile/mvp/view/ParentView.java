package com.arellomobile.mvp.view;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

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
