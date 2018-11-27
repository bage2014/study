package com.bage.study.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Select;

public interface WeatherMapper {
	
  @Select("select city,temp_lo,temp_hi,date from weather")
  List<Weather> selectAll();
  
//  @Select("SELECT * FROM blog WHERE id = #{id}")
//  Blog selectBlog(int id);
  
}
