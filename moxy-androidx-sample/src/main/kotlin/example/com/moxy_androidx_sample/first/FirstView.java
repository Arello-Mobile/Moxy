package example.com.moxy_androidx_sample.first;

import android.location.Location;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import example.com.moxy_androidx_sample.BaseView;
import example.com.moxy_androidx_sample.third.ThirdView;

public interface FirstView<M> extends BaseView, ThirdView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void firstMethod(List<M> item);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void firstCopyMethod(List<Location> item);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void firstLog(M m);

}