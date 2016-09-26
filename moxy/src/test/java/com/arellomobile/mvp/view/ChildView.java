package com.arellomobile.mvp.view;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 29.02.2016
 * Time: 9:10
 *
 * @author Savin Mikhail
 */
@GenerateViewState
@StateStrategyType(SkipStrategy.class)
public interface ChildView extends ParentView, SimpleInterface {
	@Override
	void parentOverrideMethodWithCustomStrategy();
}
