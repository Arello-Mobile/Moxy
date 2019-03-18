package example.com.androidxsample.inject

import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import example.com.androidxsample.view.MainActivity

@Module(includes = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class])
abstract class AppBindingModule {
	
	@ContributesAndroidInjector
	abstract fun bindMainActivity() : MainActivity
	
}