package com.bage;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarCopyMapper {
 
    CarCopyMapper INSTANCE = Mappers.getMapper( CarCopyMapper.class );
 
    Car merge(Car car);

    @ObjectFactory
    default Car source(Car car) {
        return car;// ... custom factory logic
    }

//    Car merge(Car car1, Car car2);

}