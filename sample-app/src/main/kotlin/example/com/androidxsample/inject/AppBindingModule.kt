package example.com.androidxsample.inject

import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import example.com.androidxsample.view.MainActivity

@Module(includes = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class])
abstract class AppBindingModule {

	abstract fun bindMainActivity() : MainActivity
	
}