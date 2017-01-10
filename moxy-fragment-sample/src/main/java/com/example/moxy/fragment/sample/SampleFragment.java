package com.example.moxy.fragment.sample;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Date: 10/01/17
 *
 * @author Alexander Blinov
 */

public class SampleFragment extends MvpAppCompatFragment implements SampleView {

    private static final String TAG = "SampleFragment";

    private static final String KEY_BUNDLE_ID = TAG + "KEY_BUNDLE_ID";


    public static SampleFragment newInstance(@NonNull String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE_ID, id);
        SampleFragment sampleFragment = new SampleFragment();
        sampleFragment.setArguments(bundle);
        return sampleFragment;
    }

    @Override
    public void setText(final String text) {
        ((TextView) getView().findViewById(R.id.fragment_sample_text_view_cont)).setText(text);
    }


    @ProvidePresenter
    SamplePresenter provideSamplePresenter() {
        return new SamplePresenter(getArguments().getString(KEY_BUNDLE_ID));
    }

    @InjectPresenter
    SamplePresenter mSamplePresenter;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }
}
