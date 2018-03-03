package view;

import com.arellomobile.mvp.MvpView;

public interface GenericMethodsView extends MvpView {
	<T> void generic(T param);

	<T extends Number> void genericWithExtends(T param);
}
