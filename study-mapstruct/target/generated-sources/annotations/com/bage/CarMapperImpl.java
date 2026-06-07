package com.bage;

import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-06T16:28:00+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
public class CarMapperImpl implements CarMapper {

    @Override
    public Car copy(Car car) {
        if ( car == null ) {
            return null;
        }

        Car car1 = new Car();

        car1.setMake( car.getMake() );
        car1.setNumberOfSeats( car.getNumberOfSeats() );
        car1.setType( car.getType() );

        return car1;
    }

    @Override
    public CarDto carToCarDto(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDto carDto = createCarDto( car );

        carDto.setSeatCount( car.getNumberOfSeats() );
        carDto.setMake( car.getMake() );
        if ( car.getType() != null ) {
            carDto.setType( car.getType().name() );
        }

        fillTank( car, carDto );

        return carDto;
    }
}
