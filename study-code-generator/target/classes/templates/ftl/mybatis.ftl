<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="Dao路径.${myClassInfo.className}Dao">

    <resultMap id="${myClassInfo.className}" type="Model路径.${myClassInfo.className}" >
    <#if myClassInfo.fieldList?exists && myClassInfo.fieldList?size gt 0>
    <#list myClassInfo.fieldList as fieldItem >
        <result column="${fieldItem.columnName}" property="${fieldItem.fieldName}" />
    </#list>
    </#if>
    </resultMap>

    <sql id="Base_Column_List">
    <#if myClassInfo.fieldList?exists && myClassInfo.fieldList?size gt 0>
    <#list myClassInfo.fieldList as fieldItem >
        `${fieldItem.columnName}`<#if fieldItem_has_next>,</#if>
    </#list>
    </#if>
    </sql>

    <insert id="insert" parameterType="java.util.Map" >
        INSERT INTO ${myClassInfo.tableName} (
        <#if myClassInfo.fieldList?exists && myClassInfo.fieldList?size gt 0>
        <#list myClassInfo.fieldList as fieldItem >
            <#if fieldItem.columnName != "Id" >
            `${fieldItem.columnName}`<#if fieldItem_has_next>,</#if>
            </#if>
        </#list>
        </#if>
        )
        VALUES(
        <#if myClassInfo.fieldList?exists && myClassInfo.fieldList?size gt 0>
        <#list myClassInfo.fieldList as fieldItem >
        <#if fieldItem.columnName != "Id" >
            <#if fieldItem.columnName="AddTime" || fieldItem.columnName="UpdateTime" >
            NOW()<#if fieldItem_has_next>,</#if>
            <#else>
            ${r"#{"}${myClassInfo.className?uncap_first}.${fieldItem.fieldName}${r"}"}<#if fieldItem_has_next>,</#if>
            </#if>
        </#if>
        </#list>
        </#if>
        )
    </insert>

    <delete id="delete" parameterType="java.util.Map" >
        DELETE FROM ${myClassInfo.tableName}
        WHERE `id` = ${r"#{id}"}
    </delete>

    <update id="update" parameterType="java.util.Map" >
        UPDATE ${myClassInfo.tableName}
        SET
        <#list myClassInfo.fieldList as fieldItem >
        <#if fieldItem.columnName != "Id" && fieldItem.columnName != "AddTime" && fieldItem.columnName != "UpdateTime" >
            ${fieldItem.columnName} = ${r"#{"}${myClassInfo.className?uncap_first}.${fieldItem.fieldName}${r"}"},
        </#if>
        </#list>
            UpdateTime = NOW()
        WHERE `id` = ${r"#{"}${myClassInfo.className?uncap_first}.id${r"}"}
    </update>


    <select id="load" parameterType="java.util.Map" resultMap="${myClassInfo.className}">
        SELECT <include refid="Base_Column_List" />
        FROM ${myClassInfo.tableName}
        WHERE `id` = ${r"#{id}"}
    </select>

    <select id="pageList" parameterType="java.util.Map" resultMap="${myClassInfo.className}">
        SELECT <include refid="Base_Column_List" />
        FROM ${myClassInfo.tableName}
        LIMIT ${r"#{offset}"}, ${r"#{pagesize}"}
    </select>

    <select id="pageListCount" parameterType="java.util.Map" resultType="int">
        SELECT count(1)
        FROM ${myClassInfo.tableName}
    </select>

</mapper>
