package example.com.moxy_androidx_sapmle.first;

import java.util.List;

import example.com.moxy_androidx_sapmle.BaseView;

public interface FirstView<M> extends BaseView{

    void firstMethod(List<M> item);

}