<?xml version="1.0"?>
<recipe>

    <#if includeLayout>
        <instantiate from="res/layout/fragment_blank.xml.ftl"
                       to="${escapeXmlAttribute(resOut)}/layout/${escapeXmlAttribute(fragmentName)}.xml" />

        <open file="${escapeXmlAttribute(resOut)}/layout/${escapeXmlAttribute(fragmentName)}.xml" />
    </#if>

    <open file="${escapeXmlAttribute(srcOut)}/ui/fragment/${subpackage}${className}.java" />

    <instantiate from="src/app_package/ui/fragment/BlankFragment.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/ui/fragment/${subpackage}${className}.java" />

    <#if includeView>
    <instantiate from="src/app_package/presentation/view/BlankView.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/presentation/view/${subpackage}${viewName}.java" />

    <open file="${escapeXmlAttribute(srcOut)}/presentation/view/${subpackage}${viewName}.java" />
    </#if>

    <#if includePresenter>
    <instantiate from="src/app_package/presentation/presenter/BlankPresenter.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/presentation/presenter/${subpackage}${presenterName}.java" />

    <open file="${escapeXmlAttribute(srcOut)}/presentation/presenter/${subpackage}${presenterName}.java" />
    </#if>
                                      
</recipe>
