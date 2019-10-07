package example.com.moxy_androidx_sample

import android.util.Log
import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
	
	override fun onFirstViewAttach() {
		super.onFirstViewAttach()
		Log.e(MainActivity.TAG, "presenter hash code : ${hashCode()}")
		viewState.printLog(10.0)
	}
	
}