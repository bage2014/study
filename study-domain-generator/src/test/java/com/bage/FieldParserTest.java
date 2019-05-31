package com.bage;

import com.bage.domain.Seller;
import com.bage.parser.ClassParser;
import com.bage.parser.FieldParser;
import com.bage.util.PrintUtils;
import org.junit.Test;

import java.lang.reflect.Field;

public class FieldParserTest {

    @Test
    public void test(){
        Field[] fields = FieldParser.getDeclaredFields(Seller.class);
        PrintUtils.print(fields);

    }

}
