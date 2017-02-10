package com.arellomobile.mvp.sample.kotlin

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Date: 03.03.2016
 * Time: 11:34
 * @author Yuri Shmakov
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface DialogView : MvpView {
    fun showDialog()
    fun hideDialog()
}