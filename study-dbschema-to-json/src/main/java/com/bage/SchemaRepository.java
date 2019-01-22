package com.bage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SchemaRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String queryTables(){
        return null;
    }


    public List<Map<String, Object>> queryTable(String tableName){
        String sql = "select column_name,column_comment,data_type  from information_schema.columns where table_name=? and table_schema='db_example'";
        return jdbcTemplate.queryForList(sql,new Object[]{tableName});
    }

}
