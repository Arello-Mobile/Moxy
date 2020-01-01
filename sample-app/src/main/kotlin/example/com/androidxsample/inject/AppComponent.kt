package example.com.androidxsample.inject

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import example.com.androidxsample.App
import javax.inject.Singleton

@Singleton
@Component(modules = [AppBindingModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {
	
	override fun inject(app: DaggerApplication)
	
	@Component.Builder
	interface Builder {
		
		@BindsInstance
		fun application(application: App): Builder
		
		fun build(): AppComponent
	}
	
}