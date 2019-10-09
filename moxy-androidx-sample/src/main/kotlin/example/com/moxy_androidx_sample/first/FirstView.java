package example.com.moxy_androidx_sample.first;

import android.location.Location;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import example.com.moxy_androidx_sample.BaseView;
import example.com.moxy_androidx_sample.third.ThirdView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface FirstView<M> extends BaseView, ThirdView {

    void firstMethod(List<M> item);

    void firstCopyMethod(List<Location> item);

    void firstLog(M m);

}