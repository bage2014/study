<#if myClassInfo.fieldList?exists && myClassInfo.fieldList?size gt 0>
    <#list myClassInfo.fieldList as fieldItem >
        <#if fieldItem.fieldClass == "Date">
            <#assign importDdate = true />
        </#if>
    </#list>
</#if>
import java.io.Serializable;
<#if importDdate>
import java.util.Date;
</#if>

/**
*  ${myClassInfo.classComment}
*
*  Created on '${.now?string('yyyy-MM-dd HH:mm:ss')}'.
*/
public class ${myClassInfo.className} implements Serializable {
    private static final long serialVersionUID = 42L;

<#if myClassInfo.fieldList?exists && myClassInfo.fieldList?size gt 0>
<#list myClassInfo.fieldList as fieldItem >
    /**
    * ${fieldItem.fieldComment}
    */
    private ${fieldItem.fieldClass} ${fieldItem.fieldName};

</#list>
</#if>

<#if myClassInfo.fieldList?exists && myClassInfo.fieldList?size gt 0>
<#list myClassInfo.fieldList as fieldItem>
    public ${fieldItem.fieldClass} get${fieldItem.fieldName?cap_first}() {
        return ${fieldItem.fieldName};
    }

    public void set${fieldItem.fieldName?cap_first}(${fieldItem.fieldClass} ${fieldItem.fieldName}) {
        this.${fieldItem.fieldName} = ${fieldItem.fieldName};
    }

</#list>
</#if>
}