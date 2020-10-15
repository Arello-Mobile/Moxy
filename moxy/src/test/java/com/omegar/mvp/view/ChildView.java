package com.omegar.mvp.view;

import com.omegar.mvp.viewstate.strategy.SkipStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 29.02.2016
 * Time: 9:10
 *
 * @author Savin Mikhail
 */
@StateStrategyType(SkipStrategy.class)
public interface ChildView extends ParentView, SimpleInterface {
	@Override
	void parentOverrideMethodWithCustomStrategy();
}
