package ru.kuchanov.samplegenericview.ui.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.kuchanov.samplegenericview.R
import ru.kuchanov.samplegenericview.model.MyModel
import ru.kuchanov.samplegenericview.mvp.presenter.GenericPresenter
import ru.kuchanov.samplegenericview.mvp.view.GenericView

class MainActivity : MvpAppCompatActivity(), GenericView<MyModel> {

    @InjectPresenter
    lateinit var presenter: GenericPresenter<MyModel>

    @ProvidePresenter
    fun providePresenter() = GenericPresenter<MyModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (findViewById(R.id.textView) as TextView).setOnClickListener { presenter.doSmth(MyModel("test")) }
    }

    override fun showType(param: MyModel) {
        Toast.makeText(this, param.toString(), Toast.LENGTH_SHORT).show()
    }
}
