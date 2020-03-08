package com.bage;

import com.bage.domain.ClassAttribute;

public class KeyUtils {

    public static String getMapKey(ClassAttribute classAttribute) {
        return classAttribute.getClassOf() + "." + classAttribute.getName();
    }

}
