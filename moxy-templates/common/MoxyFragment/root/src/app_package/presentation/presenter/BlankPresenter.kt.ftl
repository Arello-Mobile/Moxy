package ${packageName}.presentation.presenter${dotSubpackage}

import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter
import ${packageName}.presentation.view${dotSubpackage}.${viewName}

@InjectViewState
class ${presenterName} : MvpPresenter<${viewName}>() {

}
