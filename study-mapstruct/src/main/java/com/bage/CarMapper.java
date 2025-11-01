package com.bage;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {
 
    CarMapper INSTANCE = Mappers.getMapper( CarMapper.class );
 
    Car copy(Car car);

    @Mapping(source = "numberOfSeats", target = "seatCount")
    CarDto carToCarDto(Car car);

    @ObjectFactory
    default CarDto createCarDto(Car car) {
        return new CarDto();// ... custom factory logic
    }

    @AfterMapping
    default void fillTank(Car vehicle, @MappingTarget CarDto result) {
        result.setType( vehicle.getType().name());
    }
}