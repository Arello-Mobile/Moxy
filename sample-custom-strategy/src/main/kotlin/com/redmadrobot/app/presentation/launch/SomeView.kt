package com.redmadrobot.app.presentation.launch

import com.arellomobile.mvp.MvpView

interface SomeView : MvpView {
    fun toggleCheese(enable: Boolean)
}