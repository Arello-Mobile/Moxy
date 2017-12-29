package ru.kuchanov.samplegenericview.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.kuchanov.samplegenericview.mvp.view.GenericView

/**
 * Created by mohax on 29.12.2017.
 *
 * for Moxy
 */
@InjectViewState
class GenericPresenter<T> : MvpPresenter<GenericView<T>>() {
}