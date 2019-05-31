package com.bage;

import com.bage.domain.Seller;
import com.bage.parser.ClassParser;
import com.bage.util.PrintUtils;
import org.junit.Test;

public class ClassParserTest {

    @Test
    public void test(){
        Class[] cls = ClassParser.getAllClass(Seller.class);
        PrintUtils.print(cls);

    }

}
