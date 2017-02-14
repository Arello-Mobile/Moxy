package ${packageName}.ui.activity${dotSubpackage};

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ${packageName}.presentation.view${dotSubpackage}.${viewName};
import ${packageName}.presentation.presenter${dotSubpackage}.${presenterName};

import ${superClassFqcn};

<#if packageName??>
import ${packageName}.R;
</#if>

import com.arellomobile.mvp.presenter.InjectPresenter;
<#if createProvidesMethod>
import com.arellomobile.mvp.presenter.ProvidePresenter;
</#if>

public class ${className} extends ${superClassName} implements ${viewName} {
    public static final String TAG = "${className}";
	@InjectPresenter
	${presenterName} m${presenterName};

    <#if createProvidesMethod>
    @ProvidePresenter
    ${presenterName} providePresenter() {
        return new ${presenterName}();
    }
    </#if>

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
