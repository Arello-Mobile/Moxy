package com.redmadrobot.app.presentation.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.redmadrobot.app.presentation.launch.LaunchFragment
import com.redmadrobot.sample_custom_strategy.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        if (supportFragmentManager.findFragmentByTag(LaunchFragment.TAG) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.activity_main_container, LaunchFragment.newInstance(), LaunchFragment.TAG)
                    .commitNowAllowingStateLoss()
        }
    }
}
