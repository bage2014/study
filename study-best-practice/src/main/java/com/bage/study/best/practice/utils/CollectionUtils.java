package com.bage.study.best.practice.utils;

import java.util.List;
import java.util.Objects;

public class CollectionUtils {

    public static boolean isNullOrEmpty(List<?> list){
        if(Objects.isNull(list)){
            return true;
        }
        if(list.isEmpty()){
            return true;
        }
        return false;
    }

    public static boolean isNotNullAndEmpty(List<?> list){
        if(Objects.isNull(list)){
            return false;
        }
        if(list.isEmpty()){
            return false;
        }
        return true;
    }

}
