package com.example.moxy.fragment.sample;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date: 10/01/17
 *
 * @author Alexander Blinov
 */

@InjectViewState
public class SamplePresenter extends MvpPresenter<SampleView> {

    private static AtomicInteger sCounter = new AtomicInteger();

    public SamplePresenter(final String id) {
        getViewState().setText(id + "_" + sCounter.incrementAndGet());
    }
}
