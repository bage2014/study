package com.bage;

import com.bage.domain.ClassAttribute;
import com.bage.domain.FieldAttribute;
import com.bage.parser.ClassParser;
import com.bage.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 默认值生成
 */
public class CodeGenerator {
    ClassStructureGenerator mappingCodeGenerator = new ClassStructureGenerator();

    public String getCode(Class cls1, Class cls2) {
        ClassAttribute classAttribute1 = mappingCodeGenerator.getClassAttribute(cls1);
        ClassAttribute classAttribute2 = mappingCodeGenerator.getClassAttribute(cls2);
        Map<String, List<FieldAttribute>> map1 = mappingCodeGenerator.toMap(classAttribute1);
        Map<String, List<FieldAttribute>> map2 = mappingCodeGenerator.toMap(classAttribute2);

        return getCode(classAttribute1, map1, classAttribute2, map2);
    }


    private String getCode(ClassAttribute classAttribute1, Map<String, List<FieldAttribute>> map1, ClassAttribute classAttribute2, Map<String, List<FieldAttribute>> map2) {

        if (Objects.isNull(classAttribute1) || Objects.isNull(classAttribute2)) {
            return "";
        }

        String className = classAttribute1.getName();
        String cls1 = classAttribute1.getClassOf();
        String cls2 = classAttribute2.getClassOf();


        // 匹配当前类的属性
        List<FieldAttribute> fields1 = classAttribute1.getFields();
        List<FieldAttribute> fields2 = classAttribute2.getFields();

        StringBuilder res = new StringBuilder();
        res.append("\n")
                .append(" /** \n  ")
                .append(classAttribute1.getName())
                .append(" mapping \n */ \n  ");

        StringBuilder head = new StringBuilder();
        head.append("@Mapper(uses = {\n");

        StringBuilder mappingHead = new StringBuilder();
        StringBuilder mappingbody = new StringBuilder();
        // 递归匹配子类的
        for (FieldAttribute field : fields1) {

            // 查找名字相同属性
            FieldAttribute targetField = null;
            for (FieldAttribute attribute : fields2) {
                if (Objects.equals(attribute.getName(), field.getName())) {
                    targetField = attribute;
                    break;
                }
            }

            // 不存在直接返回
            if (Objects.isNull(targetField)) {
                mappingHead.append("\n    @Mapping(source = \"??\", target = \"" + field.getName() + "\", qualifiedByName = \"" + field.getName() + "Convert()\")");

                mappingbody.append("      @Named(\"" + field.getName() + "Convert\")\n" +
                        "    static " + field.getCls().getSimpleName() + " " + field.getName() + "Convert(YourType param) {\n" +
                        "        return null; // todo 自定义转换 \n" +
                        "    }\n\n");
//                Logger.debug("targetField not found , name = {}", field.getName());
                continue;
            }

            // 不用匹配的类型
            if (!ClassParser.isRecusiveClass(field.getClassOf())) {
                continue;
            }

            // 泛型类型,使用具体的泛型类型
            if (List.class == field.getCls()
                    || Map.class == field.getCls()
                    || Set.class == field.getCls()) {
                ClassAttribute classAttribute = field.getClassAttribute();
                if (Objects.isNull(classAttribute)) {
                    continue;
                }
                String name = classAttribute.getName();
                if (!ClassParser.isRecusiveClass(classAttribute.getClassOf())) {
                    continue;
                }
                head.append("        ");
                head.append(name);
                head.append("Mapping.class,\n");
            } else {
                head.append("        ");
                head.append(field.getField().getType().getSimpleName());
                head.append("Mapping.class,\n");
            }

            // 递归查找匹配
            ClassAttribute classAttribute11 = field.getClassAttribute();
            ClassAttribute classAttribute22 = targetField.getClassAttribute();
            String code = getCode(classAttribute11, map1, classAttribute22, map2);

            // 结果合并
            res.append(code)
                    .append("\n");
        }

        // 获取当前类信息
        head.append("})\n");
        if (head.length() < 30) { // 无对象
            head = new StringBuilder("@Mapper\n");
        }
        String base = head.toString() +
                ("public class {className}Mapping {\n" +
                        "\n" +
                        "    " + mappingHead.toString() + "\n" +
                        "    public {cls1} mapping({cls2} param);\n" +
                        "\n" +
                        "    " + mappingbody.toString() +
                        "}" +
                        "\n\n");
        base = base.replace("{className}", className);
        base = base.replace("{cls1}", cls1);
        base = base.replace("{cls2}", cls2);
        return res.append(base).toString();
    }
}
