package example.com.androidxsample.view

import android.util.Log
import javax.inject.Inject

class Logger @Inject constructor() {
	
	fun printErrorLog() {
		Log.e(MainActivity.TAG, "logger hash code : ${hashCode()}")
	}
	
}