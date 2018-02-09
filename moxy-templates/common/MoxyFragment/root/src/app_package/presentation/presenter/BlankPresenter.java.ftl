package ${packageName}.presentation.presenter${dotSubpackage};

<#if applicationPackage??>import ${applicationPackage}.R;</#if>
import ${packageName}.presentation.view${dotSubpackage}.${viewName};
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class ${presenterName} extends MvpPresenter<${viewName}>  {

}
