package view;

import com.omegar.mvp.MvpView;

import java.io.Serializable;

public interface GenericWithExtendsView<T extends Serializable> extends MvpView {
	void testEvent(T param);
}
