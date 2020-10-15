package example.com.moxy_androidx_sample.fifth;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import example.com.moxy_androidx_sample.fourth.FourthView;

public interface Contract {

    interface FifthView extends FourthView<String> {

    }

}
