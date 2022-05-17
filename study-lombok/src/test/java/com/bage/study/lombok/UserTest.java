package com.bage.study.lombok;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class UserTest extends TestCase {
    public void testApp(){
        String id = new User().getId();
        System.out.println(id);
    }
}
