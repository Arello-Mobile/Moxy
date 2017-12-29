package ru.kuchanov.samplegenericview.mvp.view

import com.arellomobile.mvp.MvpView

/**
 * Created by mohax on 29.12.2017.
 *
 * for Moxy
 */
interface GenericView<T> : MvpView {

    fun showType(param: T)
}