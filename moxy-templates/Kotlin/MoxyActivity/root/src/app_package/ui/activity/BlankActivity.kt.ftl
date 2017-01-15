package ${packageName}.ui.activity${dotSubpackage}

import android.content.Context
import android.content.Intent
import android.os.Bundle
<#if !extendsBaseActivity>
import com.arellomobile.mvp.MvpActivity
</#if>
import com.arellomobile.mvp.presenter.InjectPresenter
import ${packageName}.R
import ${packageName}.presentation.view${dotSubpackage}.${viewName}
import ${packageName}.presentation.presenter${dotSubpackage}.${presenterName}
<#if extendsBaseActivity>
import ${packageName}.ui.activity.BaseActivity
</#if>

class ${className} : <#if extendsBaseActivity>BaseActivity()<#else>MvpActivity()</#if>, ${viewName} {
    companion object {
        const val TAG = "${className}"
	<#if includeFactory>
        fun getIntent(context: Context): Intent = Intent(context, ${className}::class.java)
	</#if>
    }

	@InjectPresenter
	lateinit var m${presenterName}: ${presenterName}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	<#if includeLayout>
		setContentView(R.layout.${activityName})
	</#if>
	}
}
