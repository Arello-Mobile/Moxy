package com.redmadrobot.app.presentation.launch

import com.omegar.mvp.MvpView
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType

interface LaunchView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class, tag = BURGER_TAG)
    fun setBreadType(breadType: BreadType)

    @StateStrategyType(AddToEndSingleStrategy::class, tag = BURGER_TAG)
    fun toggleCheese(enable: Boolean)

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = BURGER_TAG)
    fun clearBurger(breadType: BreadType, cheeseSelected: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class, tag = BEVERAGE_TAG)
    fun setSugarCount(count: Int)

    @StateStrategyType(AddToEndSingleStrategy::class, tag = BEVERAGE_TAG)
    fun toggleLemon(enable: Boolean)

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = BEVERAGE_TAG)
    fun clearBeverage(sugarCount: Int, lemonSelected: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showOrderMade()

    companion object {
        const val BURGER_TAG = "BURGER"
        const val BEVERAGE_TAG = "BEVERAGE"

    }
}