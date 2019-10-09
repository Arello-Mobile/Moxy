package example.com.moxy_androidx_sample;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface BaseView extends MvpView {

    void testFunction();

}