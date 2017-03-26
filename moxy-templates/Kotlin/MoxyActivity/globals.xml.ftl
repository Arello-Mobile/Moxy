<?xml version="1.0"?>
<globals>

    <global id="superClassName" type="string" value="
            <#if superClass == 'baseActivity'>BaseActivity
            <#elseif superClass == 'appCompat'>MvpAppCompatActivity
            <#else>MvpActivity</#if>
        " />

    <global id="superClassFqcn" type="string" value="
            <#if superClass == 'baseActivity'>${packageName}.ui.activity.BaseActivity
            <#elseif superClass == 'appCompat'>com.arellomobile.mvp.MvpAppCompatActivity
            <#else>com.arellomobile.mvp.MvpActivity</#if>
        " />

    <#include "../common/common_globals.xml.ftl" />
    <global id="subpackage" value="<#if useSubPackage>${subPackage}/<#else></#if>" />
    <global id="dotSubpackage" value="<#if useSubPackage>.${subPackage}<#else></#if>" />
</globals>
