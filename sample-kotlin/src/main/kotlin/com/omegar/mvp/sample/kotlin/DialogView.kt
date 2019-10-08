package com.omegar.mvp.sample.kotlin

import com.omegar.mvp.MvpView
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType

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