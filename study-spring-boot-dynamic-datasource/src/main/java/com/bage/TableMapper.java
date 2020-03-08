package com.bage;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TableMapper {

    List<Table> queryByPage(@Param("dbName") String dbName, @Param("name") String name);
}
