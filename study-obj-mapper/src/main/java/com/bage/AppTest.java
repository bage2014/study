package com.bage;

/**
 * Unit test for simple App.
 */
public class AppTest {
    public static void main(String[] args) {
        //given
        Car car = new Car();

        car.setMake("mkae");
        car.setNumberOfSeats(4);

        //when
        Car2 carDto = CarMapper.INSTANCE.carToCarDto(car);

        //then
        System.out.println(carDto);
    }
}
