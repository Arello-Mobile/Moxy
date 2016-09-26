package view;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.MvpView;

/**
 * Date: 26.02.2016
 * Time: 11:49
 *
 * @author Savin Mikhail
 */
public interface ViewStateForGenericView<T> extends MvpView {
	void testEvent(T ter);
}
