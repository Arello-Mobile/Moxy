package com.arellomobile.mvp.sample.kotlin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

/**
 * Date: 03.03.2016
 * Time: 11:22
 * @author Yuri Shmakov
 */
@InjectViewState
class DialogPresenter<T> : MvpPresenter<DialogView<T, Int>>() {
    fun onShowDialogClick(data: T) {
        viewState.showDialog(data, 123)
    }

    fun onHideDialog() {
        viewState.hideDialog()
    }
}
