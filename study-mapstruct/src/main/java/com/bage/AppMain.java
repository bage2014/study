package com.bage;

/**
 * Hello world!
 *
 */
public class AppMain
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        Car car1 = new Car();
        car1.setMake("make");
        car1.setType(CarType.SEDAN);

        Car car2 = new Car();
        car2.setNumberOfSeats(12);

//        Car car3 = CarCopyMapper.INSTANCE.merge(car1,car2);
//        System.out.println(car3);

    }
}
