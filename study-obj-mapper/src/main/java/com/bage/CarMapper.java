package com.bage;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {
 
    CarMapper INSTANCE = Mappers.getMapper( CarMapper.class );
 
    Car2 carToCarDto(Car car);

}