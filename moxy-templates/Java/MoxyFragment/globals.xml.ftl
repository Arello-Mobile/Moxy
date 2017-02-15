<?xml version="1.0"?>
<globals>

    <global id="superClassName" type="string" value="
            <#if superClass == 'baseFragment'>BaseFragment
            <#elseif superClass == 'appCompat'>MvpAppCompatFragment
            <#else>MvpFragment</#if>
        " />

    <global id="superClassFqcn" type="string" value="
            <#if superClass == 'baseFragment'>${packageName}.ui.fragment.BaseFragment
            <#elseif superClass == 'appCompat'>com.arellomobile.mvp.MvpAppCompatFragment
            <#else>com.arellomobile.mvp.MvpFragment</#if>
        " />

    <global id="useSupport" type="boolean" value="${(minApiLevel lt 11)?string}" />
    <global id="resOut" value="${resDir}" />
    <global id="srcOut" value="${srcDir}/${slashedPackageName(packageName)}" />
    <global id="relativePackage" value=".ui.activity" />
    <global id="subpackage" value="<#if useSubPackage>${subPackage}/<#else></#if>" />
    <global id="dotSubpackage" value="<#if useSubPackage>.${subPackage}<#else></#if>" />
</globals>
