package ${packageName}.ui.fragment${dotSubpackage}

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ${packageName}.R
import ${packageName}.presentation.view${dotSubpackage}.${viewName}
import ${packageName}.presentation.presenter${dotSubpackage}.${presenterName}

import com.omegar.mvp.MvpFragment
import com.omegar.mvp.presenter.InjectPresenter

class ${className} : MvpFragment(), ${viewName} {
    companion object {
        const val TAG = "${className}"
<#if includeFactory>

        fun newInstance(): ${className} {
			val fragment: ${className} = ${className}()
			val args: Bundle = Bundle()
			fragment.arguments = args
			return fragment
		}
</#if>
    }

	@InjectPresenter
	lateinit var m${presenterName}: ${presenterName}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
<#if includeLayout>
        return inflater.inflate(R.layout.${fragmentName}, container, false)
</#if>
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
