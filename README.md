# MoxyX
We added X to the Moxy for make this library coolest.

[![Maven Central](https://img.shields.io/maven-central/v/tech.schoolhelper/moxy-x.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22tech.schoolhelper%22%20AND%20a:%22moxy-x%22)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MoxyX-blue.svg?style=flat)](https://android-arsenal.com/details/1/7547)

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)

Moxy is a library that helps to use MVP pattern when you do the Android Application. Without problems of lifecycle and boilerplate code!

The main idea of using Moxy:
![schematic_using](https://habrastorage.org/files/a2e/b51/8b4/a2eb518b465a4df9b47e68794519270d.gif)

See what's happening here in the [wiki](https://github.com/Arello-Mobile/Moxy/wiki).

## Diff between us and root project
1. We support androidX
2. We don't support GLOBAL and WEAK presenter type
3. We have one more strategy than core project, [AddToEndSingleTagStrategy](https://github.com/jordan1997/Moxy/blob/master/moxy/src/main/java/com/arellomobile/mvp/viewstate/strategy/AddToEndSingleTagStrategy.java)

RoadMap
- [ ] We will change the template for android studio (and Intellij), the templates will create a view interface and activity or fragment into one file, and this file and presenter keep into one package. We will remove template for Java.

## Capabilities

Moxy has a few killer features in other ways:
- _Presenter_ stay alive when _Activity_ recreated(it simplify work with multithreading)
- Automatically restore all that user see when _Activity_ recreated(including dynamic content is added)
- Capability to changes of many _Views_ from one _Presenter_

## Sample

View interface
```kotlin
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
```
Presenter
```kotlin
@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
	override fun onFirstViewAttach() {
		super.onFirstViewAttach()
		Log.e(MainActivity.TAG, "presenter hash code : ${hashCode()}")
		viewState.printLog("TEST")
	}
}
```

## Inject with Dagger2

**We have plan for implement move easies way for connect moxy with dagger, but we need to implement specific compiler for this case**
 
```kotlin
	@Inject
	lateinit var daggerPresenter: Lazy<MainPresenter>
	
	@InjectPresenter
	lateinit var presenter: MainPresenter
	
	@ProvidePresenter
	fun providePresenter(): MainPresenter = daggerPresenter.get()
```

[Here](https://github.com/jordan1997/Moxy/tree/develop/sample-github) you can see "Github" sample application.

## Wiki
For all information check [Moxy Wiki](https://github.com/Arello-Mobile/Moxy/wiki)

## Android studio and Intellij templates 
**We will change this template in future**
In order to avoid boilerplate code creating for binding activity, fragments and its presentation part, we propose to use Android Studio templates for Moxy.

Templates located in [/moxy-templates](https://github.com/jordan1997/Moxy/tree/develop/moxy-templates)

## Links
**Telegram channels from original moxy community**

[Telegram channel (en)](https://telegram.me/moxy_mvp_library)<br />
[Telegram channel (ru)](https://telegram.me/moxy_ru)<br />
[References](https://github.com/Arello-Mobile/Moxy/wiki#references)<br />
[FAQ](https://github.com/Arello-Mobile/Moxy/wiki/FAQ)

## Twitter
For connection with author of this repository use twitter
[Twitter](https://twitter.com/jordan29041997)

## Integration
### Base modules integration: 
```groovy
implementation 'tech.schoolhelper:moxy-x:1.7.0'
```
#### Java project
```groovy
annotationProcessor 'tech.schoolhelper:moxy-x-compiler:1.7.0'
```
#### Kotlin
```groovy
apply plugin: 'kotlin-kapt'
```
```groovy
kapt 'tech.schoolhelper:moxy-x-compiler:1.7.0'
```
### Default android module integration
For additional base view classes `MvpActivity` and `MvpFragment` add this:
```groovy
implementation 'tech.schoolhelper:moxy-x-android:1.7.0'
```
### AppCompat module integration
If you use AppCompat, use `MvpAppCompatActivity` and `MvpAppCompatFragment` add this:
```groovy
implementation 'tech.schoolhelper:moxy-x-app-compat:1.7.0'
```
### AndroidX module integration
If you use AndroidX, use `MvpAppCompatActivity` and `MvpAppCompatFragment` add this:
```groovy
implementation 'tech.schoolhelper:moxy-x-androidx:1.7.0'
```
### AndroidX(Google material) module integration
If you use google material, use `MvpBottomSheetDialogFragment` add this:
```groovy
implementation 'tech.schoolhelper:moxy-x-material:1.7.0'
```

## ProGuard
MoxyX is completely without reflection! No special ProGuard rules required.

## License
```
The MIT License (MIT)

Copyright (c) 2016 Arello Mobile

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
