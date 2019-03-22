package ${packageName}.ui.activity${dotSubpackage}

import android.content.Context
import android.content.Intent
import android.os.Bundle

import com.omegar.mvp.presenter.InjectPresenter
import ${packageName}.R
import ${packageName}.presentation.view${dotSubpackage}.${viewName}
import ${packageName}.presentation.presenter${dotSubpackage}.${presenterName}

import ${superClassFqcn};

<#if createProvidesMethod>
import com.omegar.mvp.presenter.ProvidePresenter;
</#if>


class ${className} : ${superClassName}(), ${viewName} {
    companion object {
        const val TAG = "${className}"
	<#if includeFactory>
        fun getIntent(context: Context): Intent = Intent(context, ${className}::class.java)
	</#if>
    }

	@InjectPresenter
	lateinit var m${presenterName}: ${presenterName}

    <#if createProvidesMethod>
        @ProvidePresenter
        fun providePresenter() : ${presenterName} = ${presenterName}()
    </#if>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	<#if includeLayout>
		setContentView(R.layout.${activityName})
	</#if>
	}
}
