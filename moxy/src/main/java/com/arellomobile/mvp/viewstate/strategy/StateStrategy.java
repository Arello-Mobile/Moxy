package com.arellomobile.mvp.viewstate.strategy;

import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.Pair;
import com.arellomobile.mvp.viewstate.ViewCommand;

/**
 * Date: 17.12.2015
 * Time: 11:21
 *
 * @author Yuri Shmakov
 */
public interface StateStrategy
{
	/**
	 * Called immediately after
	 * {@link com.arellomobile.mvp.viewstate.MvpViewState} receive some
	 * command. Will not be called before re-apply to some other
	 * {@link MvpView}
	 *
	 * @param currentState current state of
	 * {@link com.arellomobile.mvp.viewstate.MvpViewState}. Each {@link Pair}
	 * contains commands to execute and params of this command.
	 * @param incomingState command for apply to {@link MvpView} This
	 * {@link Pair} contains commands to execute and params of this command.
	 * @param <View> type of incoming view
	 */
	<View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingState);

	/**
	 * Called immediately after command applied to {@link MvpView}. Also called
	 * after re-apply to other views.
	 *
	 * @param currentState current state of
	 * {@link com.arellomobile.mvp.viewstate.MvpViewState}. Each {@link Pair}
	 * contains commad to execute and params of this command.
	 * @param incomingState applied command to {@link MvpView} This
	 * {@link Pair} contains executed command and params of this command.
	 * @param <View> type of incoming view
	 */
	<View extends MvpView> void afterApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingState);
}
