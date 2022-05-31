package com.bage;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        Car car = new Car();
        car.setMake("make");
        car.setType(CarType.SEDAN);
        car.setNumberOfSeats(12);
        CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);

        System.out.println(carDto);
    }
}
