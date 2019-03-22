package com.omegar.mvp.sample.kotlin

import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter

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
