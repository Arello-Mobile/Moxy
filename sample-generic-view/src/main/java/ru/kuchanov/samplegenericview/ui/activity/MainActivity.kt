package ru.kuchanov.samplegenericview.ui.activity

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.kuchanov.samplegenericview.R
import ru.kuchanov.samplegenericview.model.MyModel
import ru.kuchanov.samplegenericview.mvp.presenter.GenericPresenter
import ru.kuchanov.samplegenericview.mvp.view.GenericView

class MainActivity : MvpAppCompatActivity(), GenericView<MyModel> {

    @InjectPresenter
    lateinit var presenter: GenericPresenter<MyModel>

    fun providePresenter() = GenericPresenter<MyModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
