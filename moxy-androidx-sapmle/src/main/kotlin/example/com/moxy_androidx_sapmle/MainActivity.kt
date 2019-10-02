package example.com.moxy_androidx_sapmle

import android.os.Bundle
import android.util.Log
import com.omegar.mvp.MvpAppCompatActivity
import com.omegar.mvp.presenter.InjectPresenter

class MainActivity : BaseActivity(), MainView, SecondInterface {

	override fun firstMethod() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	@InjectPresenter
	internal lateinit var presenter: MainPresenter
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
//		setContentView(R.layout.activity_main)
	}
	
	override fun printLog(msg: String) {
		Log.e(TAG, "printLog : msg : $msg activity hash code : ${hashCode()}")
	}

	override fun second() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	
	companion object {
		const val TAG = "MoxyDebug"
	}
}
