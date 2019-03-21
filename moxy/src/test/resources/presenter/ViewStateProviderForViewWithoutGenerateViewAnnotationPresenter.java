package presenter;

import com.omegar.mvp.InjectViewState;
import com.omegar.mvp.MvpPresenter;

import view.WithoutGenerateViewAnnotationView;

/**
 * Date: 26.02.2016
 * Time: 17:56
 *
 * @author Savin Mikhail
 */
@InjectViewState
public class ViewStateProviderForViewWithoutGenerateViewAnnotationPresenter extends MvpPresenter<WithoutGenerateViewAnnotationView> {
}
