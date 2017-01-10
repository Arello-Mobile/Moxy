package com.example.moxy.fragment.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private static AtomicInteger sCounter = new AtomicInteger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.activity_main_button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String nextTag = String.valueOf(sCounter.incrementAndGet());

                push(SampleFragment.newInstance(nextTag),nextTag);
            }
        });
    }

    public void push(Fragment fragment, String tag) {

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.activity_main_container, fragment, tag)
                .addToBackStack(null)
                .commit();

        getSupportFragmentManager().executePendingTransactions();
    }
}
