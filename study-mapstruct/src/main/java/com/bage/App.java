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

        Car cra = CarMapper.INSTANCE.copy(car);
        System.out.println(cra);

        Car car1 = new Car();
        car1.setMake("make");
        car1.setType(CarType.SEDAN);

        Car car2 = new Car();
        car2.setNumberOfSeats(12);
        CarCopyMapper.INSTANCE.source(car1);
        Car car3 = CarCopyMapper.INSTANCE.merge(car2);
        System.out.println(car3);

    }
}
