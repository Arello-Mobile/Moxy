package example.com.moxy_androidx_sample.second;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import example.com.moxy_androidx_sample.BaseView;

public interface SecondView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void secondMethod();

}