package com.redmadrobot.app.presentation.launch


import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class LaunchPresenter : MvpPresenter<LaunchView>() {

    val launch = Launch()

    init {
        onClearBurgerClicked()
        onClearBeverageClicked()
    }
    fun onToggleBreadClicked() {
        launch.toggleBread()
        viewState.setBreadType(launch.breadType)
    }

    fun onToggleCheeseClicked() {
        launch.toggleCheese()
        viewState.toggleCheese(launch.cheeseSelected)
    }

    fun onClearBurgerClicked() {
        launch.clearBurger()
        viewState.clearBurger(launch.breadType,launch.cheeseSelected)
    }

    fun onToggleLemonClicked() {
        launch.toggleLemon()
        viewState.toggleLemon(launch.lemonSelected)
    }

    fun onApplySugarClicked(count: Int) {
        launch.sugarCount = count
        viewState.setSugarCount(count)
    }

    fun onClearBeverageClicked() {
        launch.clearBeverage()
        viewState.clearBeverage(launch.sugarCount,launch.lemonSelected)
    }

    fun onMakeOrderClicked() {
        viewState.showOrderMade()
    }

}

class Launch {

    var breadType: BreadType = BreadType.BLACK
        private set

    var cheeseSelected: Boolean = DEFAULT_CHEESE
        private set

    var lemonSelected: Boolean = DEFAULT_LEMON
        private set

    var sugarCount: Int = DEFAULT_SUGAR_COUNT
        set (value) {
            field = when (value) {
                in Int.MIN_VALUE..0 -> 0
                else -> value
            }
        }


    fun clearBurger() {
        breadType = DEFAULT_BREAD
        cheeseSelected = DEFAULT_CHEESE
    }

    fun clearBeverage() {
        lemonSelected = DEFAULT_LEMON
        sugarCount = DEFAULT_SUGAR_COUNT
    }

    fun toggleBread() {
        breadType = when (breadType) {
            BreadType.BLACK -> BreadType.WHITE
            BreadType.WHITE -> BreadType.BLACK
        }
    }

    fun toggleCheese() {
        cheeseSelected = !cheeseSelected
    }

    fun toggleLemon() {
        lemonSelected = !lemonSelected
    }

    companion object {
        val DEFAULT_BREAD = BreadType.BLACK
        val DEFAULT_CHEESE = false
        val DEFAULT_LEMON = true
        val DEFAULT_SUGAR_COUNT = 0
    }
}
