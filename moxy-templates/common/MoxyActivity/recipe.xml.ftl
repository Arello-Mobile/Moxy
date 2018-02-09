<?xml version="1.0"?>
<recipe>

    <#if includeLayout>
        <instantiate from="res/layout/activity_blank.xml.ftl"
                     to="${escapeXmlAttribute(resOut)}/layout/${escapeXmlAttribute(activityName)}.xml" />

        <open file="${escapeXmlAttribute(resOut)}/layout/${escapeXmlAttribute(activityName)}.xml" />
    </#if>

    <open file="${escapeXmlAttribute(srcOut)}/ui/activity/${subpackage}${className}.${ktOrJavaExt}" />

    <instantiate from="src/app_package/ui/activity/BlankActivity.${ktOrJavaExt}.ftl"
                   to="${escapeXmlAttribute(srcOut)}/ui/activity/${subpackage}${className}.${ktOrJavaExt}" />

    <#if includeView>
    <instantiate from="src/app_package/presentation/view/BlankView.${ktOrJavaExt}.ftl"
                   to="${escapeXmlAttribute(srcOut)}/presentation/view/${subpackage}${viewName}.${ktOrJavaExt}" />

    <open file="${escapeXmlAttribute(srcOut)}/presentation/view/${subpackage}${viewName}.${ktOrJavaExt}" />
    </#if>

    <#if includePresenter>
    <instantiate from="src/app_package/presentation/presenter/BlankPresenter.${ktOrJavaExt}.ftl"
                   to="${escapeXmlAttribute(srcOut)}/presentation/presenter/${subpackage}${presenterName}.${ktOrJavaExt}" />

    <open file="${escapeXmlAttribute(srcOut)}/presentation/presenter/${subpackage}${presenterName}.${ktOrJavaExt}" />
    </#if>

    <merge from="AndroidManifest.xml.ftl"
             to="${escapeXmlAttribute(manifestOut)}/AndroidManifest.xml" />
                                      
</recipe>
