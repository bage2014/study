package com.bage.study.lombok;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class UserAccessTest extends TestCase {
    public void testApp(){
        UserAccess userAccess = new UserAccess();
        userAccess.id("id").name("name");
        System.out.println(userAccess);
    }
}
