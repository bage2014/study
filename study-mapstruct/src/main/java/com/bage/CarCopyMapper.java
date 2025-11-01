package com.bage;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarCopyMapper {
 
    CarCopyMapper INSTANCE = Mappers.getMapper( CarCopyMapper.class );
 
    Car merge(Car car, @M);

    @ObjectFactory
    default Car source(Car car) {
        return car;// ... custom factory logic
    }

}