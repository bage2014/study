package com.bage;

import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-06T16:28:00+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
public class CarCopyMapperImpl implements CarCopyMapper {

    @Override
    public Car merge(Car car) {
        if ( car == null ) {
            return null;
        }

        Car car1 = source( car );

        car1.setMake( car.getMake() );
        car1.setNumberOfSeats( car.getNumberOfSeats() );
        car1.setType( car.getType() );

        return car1;
    }
}
