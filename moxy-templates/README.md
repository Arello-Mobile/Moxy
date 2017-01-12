We've introduced you into Moxy - mvp framework for spliting application into logic layers and lifecycle managing. Now it's time to to reduce boilerplate via Android Studio Templates


<h4><b>Project structure</b></h4>
Firstly we should immobilize project structure 

<ul>
	<li> model</li>
	<li> presentation
		<ul>
		<li> presenter</li>
		<li> view</li>
	</ul></li>
    
  <li> ui
		<ul>
		<li> activity</li>
		<li> fragment</li>
	</ul></li>
</ul>

Presenter, view, activity and fragment  packages consist of logic modules.  These models are often the application's sections  (f.e. intro, offers, feed)
Check example with two Activity (CarActivity and HomeActivity) and one fragment (CarDetailsFragment) belowarDetailsFragment)

 ![Activity template](https://raw.githubusercontent.com/Arello-Mobile/Moxy/master/moxy-templates/images/project_structure.jpg)

Our goal is generate this class set using Android Studio Templates

<h4><b>Template settings</b></h4>

Adding Moxy templates to Android Studio:
* Download templates for **Java** from [Github](https://github.com/Arello-Mobile/Moxy/tree/master/moxy-templates/Java) or directly [using link](https://drive.google.com/file/d/0B0bXlVHPiZVXY2FVQkNLc1lMbW8/view?usp=sharing)
* Or download templates for **Kotlin** from [Github](https://github.com/Arello-Mobile/Moxy/tree/master/moxy-templates/Kotlin)
* Copy files to ANDROID_STUDIO_DIR/plugins/android/lib/templates/activities
* Restart Android Studio

Managing hot keys for templates quick access:
<ul>
	<li>Open settings-> Keymap</li>	
	<li>Enter "Moxy" to search box</li>	
	<li>Add key combination (A prefer Alt + A for Moxy Activity and Alt + F for Moxy Fragment)</li>
</ul>

 ![Activity template](https://raw.githubusercontent.com/Arello-Mobile/Moxy/master/moxy-templates/images/keymap.jpg)

<h4><b>Temlates using</b></h4>

* Click your **ROOT** package and then press Alt + A.
* In Activity Name box enter "MyFirstMoxyActivity"

Other fields will fill automaticly

 ![Activity template](https://raw.githubusercontent.com/Arello-Mobile/Moxy/master/moxy-templates/images/activity_template.jpg)

Finaly print in the Package Name field yoor subpackage name (f.e. first) and press Finish.

<b>Attention</b> while your Android Studio upgrading all the custom templates can be delayed. In this case it is necessary to import them again
