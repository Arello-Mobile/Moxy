package example.com.moxy_androidx_sample.third;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import example.com.moxy_androidx_sample.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ThirdView extends BaseView {

    void thirdMethod();

}
