package example.com.moxy_androidx_sapmle

import android.os.Bundle
import android.util.Log
import com.omegar.mvp.MvpAppCompatActivity
import com.omegar.mvp.MvpView
import com.omegar.mvp.presenter.InjectPresenter

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
