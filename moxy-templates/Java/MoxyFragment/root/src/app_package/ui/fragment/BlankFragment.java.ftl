package ${packageName}.ui.fragment${dotSubpackage};

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<#if applicationPackage??>
import ${applicationPackage}.R;
</#if>
import ${packageName}.presentation.view${dotSubpackage}.${viewName};
import ${packageName}.presentation.presenter${dotSubpackage}.${presenterName};

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class ${className} extends MvpFragment implements ${viewName} {
    public static final String TAG = "${className}";
	@InjectPresenter
	${presenterName} m${presenterName};
<#if includeFactory>
    public static ${className} newInstance() {
        ${className} fragment = new ${className}();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }
</#if>
    @Override
    public View onCreateView(final LayoutInflater inflater, final   ViewGroup container,
            final Bundle savedInstanceState) {
<#if includeLayout>
        return inflater.inflate(R.layout.${fragmentName}, container, false);
</#if>
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
