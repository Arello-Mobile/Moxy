package com.arellomobile.mvp.view;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.InjectViewStatePresenter;

public class DelegateLocalPresenterTestView extends CounterTestView {
    @InjectPresenter
    public InjectViewStatePresenter mInjectViewStatePresenter;
}