package com.bage;

import com.bage.domain.ClassAttribute;
import com.bage.domain.FieldAttribute;
import com.bage.parser.ClassParser;
import com.bage.parser.FieldParser;
import com.bage.parser.GenericParser;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 默认值生成
 */
public class CodeGenerator {

    public String getCode(ClassAttribute classAttribute) {
        String className = classAttribute.getName();
        String cls = classAttribute.getClassOf();
        // 获取当前类信息
        String res = "public class {className}Mapping {\n" +
                "\n" +
                "    public String mapping({cls} param) {\n" +
                "    // TODO \n" +
                "    }\n" +
                "\n" +
                "}";
        res = res.replace("{className}",className);
        res = res.replace("{cls}",cls);
        return res;
    }

}
