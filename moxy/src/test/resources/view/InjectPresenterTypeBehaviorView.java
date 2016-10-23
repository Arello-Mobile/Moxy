package view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.factory.MockPresenterFactory;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import presenter.PositiveParamsViewPresenter;

/**
 * Date: 25.02.2016
 * Time: 14:50
 *
 * @author Savin Mikhail
 */
public class InjectPresenterTypeBehaviorView implements MvpView {
	@InjectPresenter(tag = "", type = PresenterType.LOCAL)
	private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mTagLocalPresenter;

	@InjectPresenter(factory = MockPresenterFactory.class, type = PresenterType.LOCAL)
	private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mFactoryLocalPresenter;

	@InjectPresenter(presenterId = "", type = PresenterType.LOCAL)
	private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mPresenterIdLocalPresenter;

	@InjectPresenter(tag = "")
	private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mTagLocalPresenter2;

	@InjectPresenter(factory = MockPresenterFactory.class)
	private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mFactoryLocalPresenter2;

	@InjectPresenter(presenterId = "")
	private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mPresenterIdLocalPresenter2;

	@InjectPresenter(tag = "", factory = MockPresenterFactory.class)
	private PositiveParamsViewPresenter<InjectPresenterTypeBehaviorView> mFactoryTagPresenter;
}
