package example.com.moxy_androidx_sample

import android.util.Log
import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter
import example.com.moxy_androidx_sample.contract.Contract

class MainPresenter : MvpPresenter<Contract.MainView>() {
	
	override fun onFirstViewAttach() {
		super.onFirstViewAttach()
		Log.e(MainActivity.TAG, "presenter hash code : ${hashCode()}")
		viewState.printLog(10.0, "Kek")
	}
	
}