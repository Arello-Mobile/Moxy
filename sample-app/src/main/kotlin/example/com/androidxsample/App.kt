package example.com.androidxsample

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class App : DaggerApplication() {
	
	override fun applicationInjector(): AndroidInjector<DaggerApplication> {
		return DaggerAppComponent.builder()
				.application(this)
				.build()
	}
	
}