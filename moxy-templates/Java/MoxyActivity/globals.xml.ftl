<?xml version="1.0"?>
<globals>

    <global id="superClassName" type="string" value="
            <#if superClass == 'baseActivity'>BaseActivity
            <#elseif superClass == 'appCompat'>MvpAppCompatActivity
            <#else>MvpActivity</#if>
        " />

    <global id="superClassFqcn" type="string" value="
            <#if superClass == 'baseActivity'>${packageName}.ui.activity.BaseActivity
            <#elseif superClass == 'appCompat'>com.omegar.mvp.MvpAppCompatActivity
            <#else>com.omegar.mvp.MvpActivity</#if>
        " />

    <global id="manifestOut" value="${manifestDir}" />
    <global id="useSupport" type="boolean" value="${(minApiLevel lt 11)?string}" />
    <global id="resOut" value="${resDir}" />
    <global id="srcOut" value="${srcDir}/${slashedPackageName(packageName)}" />
    <global id="relativePackage" value=".ui.activity" />
    <global id="subpackage" value="<#if useSubPackage>${subPackage}/<#else></#if>" />
    <global id="dotSubpackage" value="<#if useSubPackage>.${subPackage}<#else></#if>" />
</globals>
