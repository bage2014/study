package com.bage;

import com.bage.domain.ClassAttribute;
import com.bage.domain.ComplexDomain;
import com.bage.domain.FieldAttribute;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MappingCodeGeneratorTest {

    @Test
    public void getInstance(){
        ClassStructureGenerator mappingCodeGenerator = new ClassStructureGenerator();
        ClassAttribute classAttribute = mappingCodeGenerator.getClassAttribute(ComplexDomain.class);
//        System.out.println(gson.toJson(classAttribute));

        CodeGenerator codeGenerator = new CodeGenerator();

//        List<List<FieldAttribute>> classArray = mappingCodeGenerator.toList(classAttribute);
//        prints(classArray);

        Map<String,List<FieldAttribute>> map = mappingCodeGenerator.toMap(classAttribute);
        prints(map);


        System.out.println(codeGenerator.getCode(classAttribute,map,classAttribute,map));
    }

    private void prints(Map<String, List<FieldAttribute>> map) {
        Set<Map.Entry<String, List<FieldAttribute>>> entries = map.entrySet();
        map.forEach((key,value) -> {
            System.out.print(key);
            System.out.print("-");
            System.out.print(Arrays.toString(value.stream().map(item -> item.getName()).collect(Collectors.toList()).toArray()));
            System.out.println();
        });

    }

    private void prints(List<List<FieldAttribute>> classArray) {
        for (List<FieldAttribute> classAttributes : classArray) {
            for (FieldAttribute classAttribute : classAttributes) {
                System.out.print(classAttribute.getClassOf() + ":" + classAttribute.getName() + "\t");
            }
            System.out.println();
        }
    }

}
