package view;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Date: 26.02.2016
 * Time: 12:09
 *
 * @author Savin Mikhail
 */
@GenerateViewState
public interface ViewStateChildWithIncorrectStrategyTagView extends ViewStateParentView, ViewStateParentStrategyTagView {

}
