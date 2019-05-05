package com.bage;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void shouldMapCarToDto() {
        //given
        Car car = new Car( "Morris", 5, CarType.SEDAN );

        //when
        CarDto carDto = CarMapper.INSTANCE.carToCarDto( car );

        //then
        Assertions.assertThat( carDto ).isNotNull();
        Assertions.assertThat( carDto.getMake() ).isEqualTo( "Morris" );
        Assertions.assertThat( carDto.getSeatCount() ).isEqualTo( 5 );
        Assertions.assertThat( carDto.getType() ).isEqualTo( "SEDAN" );
    }
}
