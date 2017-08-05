package com.arellomobile.mvp.viewstate.strategy;

import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;

/**
 * Cautions:
 * <ul>
 *     <li>Don't rearrange current state</li>
 *     <li>Don't insert commands inside existing current state – only put to end of it</li>
 *     <li>Be careful if remove commands by another type. If you make it, be sure that inside your view method you fully override view changes</li>
 * </ul>
 *
 * Date: 17.12.2015
 * Time: 11:21
 *
 * @author Yuri Shmakov
 */
public interface StateStrategy {
	/**
	 * Called immediately after
	 * {@link com.arellomobile.mvp.viewstate.MvpViewState} receive some
	 * command. Will not be called before re-apply to some other
	 * {@link MvpView}
	 *
	 * @param currentState    current state of
	 *                        {@link com.arellomobile.mvp.viewstate.MvpViewState}. Each {@link ViewCommand}
	 *                        contains self parameters.
	 * @param incomingCommand command for apply to {@link MvpView} This
	 *                        {@link ViewCommand} contains params of this command.
	 * @param <View>          type of incoming view
	 */
	<View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand);

	/**
	 * Called immediately after command applied to {@link MvpView}. Also called
	 * after re-apply to other views.
	 *
	 * @param currentState    current state of
	 *                        {@link com.arellomobile.mvp.viewstate.MvpViewState}. Each {@link ViewCommand}
	 *                        contains self parameters.
	 * @param incomingCommand applied command to {@link MvpView} This
	 *                        {@link ViewCommand} contains params of this command.
	 * @param <View>          type of incoming view
	 */
	<View extends MvpView> void afterApply(List<ViewCommand<View>> currentState, ViewCommand<View> incomingCommand);
}
