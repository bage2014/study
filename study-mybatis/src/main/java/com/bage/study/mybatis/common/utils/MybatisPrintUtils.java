package com.bage.study.mybatis.common.utils;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MybatisPrintUtils {

    /**
     * 格式化 SQL
     *
     * @param sqlSessionFactory
     * @param mapperClass
     * @param methodName
     * @param parameterObject
     * @return
     */
    public static String formatSql(SqlSessionFactory sqlSessionFactory, Class mapperClass, String methodName, Object parameterObject) {
        MappedStatement mappedStatement = sqlSessionFactory.getConfiguration().getMappedStatement(mapperClass.getName() + "." + methodName);
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
        return formatSql(sql, parameterObject, parameterMappingList);
    }

    /**
     * 格式化 SQL
     *
     * @param sql
     * @param parameterObject
     * @param parameterMappingList
     * @return
     */
    public static String formatSql(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {
        // 校验
        if (Objects.isNull(sql)) {
            return null;
        }
        if ("".equals(sql.trim())) {
            return "";
        }

        //  去除换行啥的
        sql = sql.replaceAll("[\\s\n ]+", " ");

        // 没有传递参数，直接返回 SQL
        if (Objects.isNull(parameterObject) || Objects.isNull(parameterMappingList) || parameterMappingList.isEmpty()) {
            return sql;
        }

        String newSql = sql;
        try {
            if (parameterMappingList != null) {
                Class parameterObjectClass = parameterObject.getClass();

                // 如果参数是StrictMap且Value类型为Collection，获取key="list"的属性，这里主要是为了处理<foreach>循环时传入List这种参数的占位符替换
                // 例如select * from xxx where id in <foreach
                // collection="list">...</foreach>
                if (isStrictMap(parameterObjectClass)) {
                    DefaultSqlSession.StrictMap<Collection<?>> strictMap = (DefaultSqlSession.StrictMap<Collection<?>>) parameterObject;

                    Collection collection = strictMap.get("list");
                    if (isList(collection.getClass())) {
                        sql = handleCollectionParameter(sql, collection);
                    }
                } else if (isMap(parameterObjectClass)) {
                    // 如果参数是Map则直接强转，通过map.get(key)方法获取真正的属性值
                    // 这里主要是为了处理<insert>、<delete>、<update>、<select>时传入parameterType为map的场景
                    Map<?, ?> paramMap = (Map<?, ?>) parameterObject;
                    sql = handleMapParameter(sql, paramMap, parameterMappingList);
                } else {
                    // 通用场景，比如传的是一个自定义的对象或者八种基本数据类型之一或者String
                    sql = handleCommonParameter(sql, parameterMappingList, parameterObjectClass, parameterObject);
                }
            }
        } catch (Exception e) {
            // 占位符替换过程中出现异常，则返回没有替换过占位符但是格式美化过的sql，这样至少保证sql语句比BoundSql中的sql更好看
            return newSql;
        }

        return sql;
    }


    /**
     * 处理通用场景
     *
     * @param sql
     * @param parameterMappingList
     * @param parameterObjectClass
     * @param parameterObject
     * @return
     * @throws Exception
     */
    private static String handleCommonParameter(String sql, List<ParameterMapping> parameterMappingList,
                                                Class<?> parameterObjectClass, Object parameterObject) throws Exception {
        for (ParameterMapping parameterMapping : parameterMappingList) {
            String propertyValue = null;
            // 基本数据类型或者基本数据类型的包装类，直接toString即可获取其真正的参数值，其余直接取paramterMapping中的property属性即可
            if (isPrimitiveOrPrimitiveWrapper(parameterObjectClass)) {
                propertyValue = parameterObject.toString();
            } else {
                String propertyName = parameterMapping.getProperty();

                Field field = parameterObjectClass.getDeclaredField(propertyName);
                // 要获取Field中的属性值，这里必须将私有属性的accessible设置为true
                field.setAccessible(true);
                propertyValue = String.valueOf(field.get(parameterObject));
                if (parameterMapping.getJavaType().isAssignableFrom(String.class)) {
                    propertyValue = "\"" + propertyValue + "\"";
                }
                field.setAccessible(false);
            }

            sql = sql.replaceFirst("\\?", propertyValue);
        }

        return sql;
    }

    /**
     * 处理Map场景
     *
     * @param sql
     * @param paramMap
     * @param parameterMappingList
     * @return
     */
    private static String handleMapParameter(String sql, Map<?, ?> paramMap, List<ParameterMapping> parameterMappingList) {
        for (ParameterMapping parameterMapping : parameterMappingList) {
            Object propertyName = parameterMapping.getProperty();
            Object propertyValue = paramMap.get(propertyName);
            if (propertyValue != null) {
                if (propertyValue.getClass().isAssignableFrom(String.class)) {
                    propertyValue = "\"" + propertyValue + "\"";
                }

                sql = sql.replaceFirst("\\?", propertyValue.toString());
            }
        }
        return sql;
    }

    /**
     * 处理 Collection 场景
     *
     * @param sql
     * @param collection
     * @return
     */
    private static String handleCollectionParameter(String sql, Collection collection) {
        if (Objects.nonNull(collection)) {
            for (Object obj : collection) {
                String value = null;
                Class clazz = obj.getClass();

                // 只处理基本数据类型、基本数据类型的包装类、String这三种
                // 如果是复合类型也是可以的，不过复杂点且这种场景较少，写代码的时候要判断一下要拿到的是复合类型中的哪个属性
                if (isPrimitiveOrPrimitiveWrapper(clazz)) {
                    value = obj.toString();
                } else if (clazz.isAssignableFrom(String.class)) {
                    value = "\"" + obj.toString() + "\"";
                }

                sql = sql.replaceFirst("\\?", value);
            }
        }

        return sql;
    }


    /**
     * 是否DefaultSqlSession的内部类StrictMap
     *
     * @param clazz
     * @return
     */
    public static boolean isStrictMap(Class clazz) {
        return clazz.isAssignableFrom(DefaultSqlSession.StrictMap.class);
    }

    /**
     * 是否基本数据类型或者基本数据类型的包装类
     *
     * @param clazz
     * @return
     */
    public static boolean isPrimitiveOrPrimitiveWrapper(Class clazz) {

        // 基本类型
        if (clazz.isPrimitive()) {
            return true;
        }

        // 基本类型包装类
        if (clazz.isAssignableFrom(Byte.class)
                || clazz.isAssignableFrom(Short.class)
                || clazz.isAssignableFrom(Integer.class)
                || clazz.isAssignableFrom(Long.class)
                || clazz.isAssignableFrom(Double.class)
                || clazz.isAssignableFrom(Float.class)
                || clazz.isAssignableFrom(Character.class)
                || clazz.isAssignableFrom(Boolean.class)) {
            return true;
        }

        return false;
    }

    /**
     * 是否List的实现类
     *
     * @param clazz
     * @return
     */
    public static boolean isList(Class clazz) {
        Class[] interfaceClasses = clazz.getInterfaces();
        for (Class interfaceClass : interfaceClasses) {
            if (interfaceClass.isAssignableFrom(List.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否Map的实现类
     *
     * @param clazz
     * @return
     */
    public static boolean isMap(Class clazz) {
        Class[] interfaceClasses = clazz.getInterfaces();
        for (Class interfaceClass : interfaceClasses) {
            if (interfaceClass.isAssignableFrom(Map.class)) {
                return true;
            }
        }
        return false;
    }

}
