package example.com.moxy_androidx_sample.first;

import java.util.List;

import example.com.moxy_androidx_sample.BaseView;
import example.com.moxy_androidx_sample.third.ThirdView;

public interface FirstView<M> extends BaseView, ThirdView {

    void firstMethod(List<M> item);

}