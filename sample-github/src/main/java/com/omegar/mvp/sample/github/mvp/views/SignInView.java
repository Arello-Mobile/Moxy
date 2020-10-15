package com.omegar.mvp.sample.github.mvp.views;

import com.omegar.mvp.MvpView;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.SkipStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 15.01.2016
 * Time: 18:41
 *
 * @author Yuri Shmakov
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface SignInView extends MvpView {
	void startSignIn();

	void finishSignIn();

	void failedSignIn(String message);

	void hideError();

	void hideFormError();

	void showFormError(Integer emailError, Integer passwordError);

	@StateStrategyType(SkipStrategy.class)
	void successSignIn();
}