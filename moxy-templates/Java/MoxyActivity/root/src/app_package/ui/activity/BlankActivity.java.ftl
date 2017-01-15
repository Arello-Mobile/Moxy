package ${packageName}.ui.activity${dotSubpackage};

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ${packageName}.presentation.view${dotSubpackage}.${viewName};
import ${packageName}.presentation.presenter${dotSubpackage}.${presenterName};

<#if extendsBaseActivity><#if applicationPackage??>import ${applicationPackage}.ui.activity.BaseActivity;</#if><#else></#if>
<#if extendsBaseActivity!!>import com.arellomobile.mvp.MvpActivity;</#if>

<#if applicationPackage??>
import ${applicationPackage}.R;
</#if>

import com.arellomobile.mvp.presenter.InjectPresenter;

public class ${className} extends MvpActivity implements ${viewName} {
    public static final String TAG = "${className}";
	@InjectPresenter
	${presenterName} m${presenterName};

	<#if includeFactory>
    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, ${className}.class);

        return intent;
    }
	</#if>

	<#if includeLayout>

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.${activityName});
	}
	</#if>
}
