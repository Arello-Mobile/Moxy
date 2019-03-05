package com.arellomobile.mvp.viewstate;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

/**
 * Date: 16-Dec-15
 * Time: 16:59
 *
 * @author Alexander Blinov
 */
public abstract class ViewCommand<View extends MvpView> {
    private final String tag;

    private final Class<? extends StateStrategy> stateStrategyType;

    protected ViewCommand(String tag, Class<? extends StateStrategy> stateStrategyType) {
        this.tag = tag;
        this.stateStrategyType = stateStrategyType;
    }

    public abstract void apply(View view);

    public String getTag() {
        return tag;
    }

    public Class<? extends StateStrategy> getStrategyType() {
        return stateStrategyType;
    }
}
