package example.com.moxy_androidx_sample.third;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import example.com.moxy_androidx_sample.BaseView;

public interface ThirdView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void thirdMethod();

}
