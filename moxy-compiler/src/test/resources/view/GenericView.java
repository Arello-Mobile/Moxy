package view;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.MvpView;

public interface GenericView<T> extends MvpView {
	void testEvent(T param);
}
