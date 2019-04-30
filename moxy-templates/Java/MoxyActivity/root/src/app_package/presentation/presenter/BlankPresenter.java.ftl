package ${packageName}.presentation.presenter${dotSubpackage};

<#if applicationPackage??>import ${applicationPackage}.R;</#if>
import ${packageName}.presentation.view${dotSubpackage}.${viewName};
import com.omegar.mvp.InjectViewState;
import com.omegar.mvp.MvpPresenter;

@InjectViewState
public class ${presenterName} extends MvpPresenter<${viewName}>  {

}
