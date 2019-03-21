package view;

import com.omegar.mvp.MvpView;

public interface GenericView<T> extends MvpView {
	void testEvent(T param);
}
