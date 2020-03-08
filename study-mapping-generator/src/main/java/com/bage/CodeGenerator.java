package com.bage;

import com.bage.domain.ClassAttribute;
import com.bage.domain.FieldAttribute;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 默认值生成
 */
public class CodeGenerator {

//    public String getCode() {

//    }

    public String getCode(ClassAttribute classAttribute1,Map<String, List<FieldAttribute>> map1, ClassAttribute classAttribute2,Map<String, List<FieldAttribute>> map2) {

        String className = classAttribute1.getName();
        String cls1 = classAttribute1.getClassOf();
        String cls2 = classAttribute2.getClassOf();

        // 获取当前类信息
        String res = "public class {className}Mapping {\n" +
                "\n" +
                "    public {cls1} mapping({cls2} param) {\n" +
                "    // TODO \n" +
                "    }\n" +
                "\n" +
                "}" +
                "\n\n\n";
                res = res.replace("{className}",className);
        res = res.replace("{cls1}",cls1);
        res = res.replace("{cls2}",cls2);

        List<FieldAttribute> fields = classAttribute1.getFields();
        fields.forEach(item -> {
            ClassAttribute classAttribute = item.getClassAttribute();
            if(Objects.nonNull(classAttribute)){
                String mapKey = KeyUtils.getMapKey(classAttribute);
                List<FieldAttribute> fieldAttributes = map2.get(mapKey);
//                if(){ // todo
//
//                }

            }
        });
        return "";
    }
}
