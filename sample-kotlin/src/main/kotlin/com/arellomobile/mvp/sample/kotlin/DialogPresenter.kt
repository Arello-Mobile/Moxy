package com.arellomobile.mvp.sample.kotlin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

/**
 * Date: 03.03.2016
 * Time: 11:22
 * @author Yuri Shmakov
 */
@InjectViewState
class DialogPresenter : MvpPresenter<DialogView>() {
    fun onShowDialogClick() {
        viewState.showDialog()
    }

    fun onHideDialog() {
        viewState.hideDialog()
    }
}
