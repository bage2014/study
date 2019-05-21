package com.bage;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PathDefinitionMapper {

    List<PathDefinition> queryAll();

}
