package com.bage.study.common.repo.mysql.mybatis.plus.utils;

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
