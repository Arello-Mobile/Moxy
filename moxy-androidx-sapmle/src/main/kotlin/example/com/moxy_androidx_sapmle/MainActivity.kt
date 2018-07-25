package example.com.moxy_androidx_sapmle

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.presenter.InjectPresenter

interface MainView : MvpView {
	fun printLog(msg: String)
}

class MainActivity : MvpAppCompatActivity(), MainView {
	
	@InjectPresenter
	internal lateinit var presenter: MainPresenter
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
	}
	
	override fun printLog(msg: String) {
		Log.e(TAG, "printLog : msg : $msg activity hash code : ${hashCode()}")
	}
	
	companion object {
		const val TAG = "MoxyDebug"
	}
}
