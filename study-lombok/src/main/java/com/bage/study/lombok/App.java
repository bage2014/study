package com.bage.study.lombok;

import lombok.val;

import java.util.HashSet;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        val sets = new HashSet<String>();

        sets = new HashSet<String>();

        System.out.println(new User("id","name").getId());
        System.out.println( "Hello World!" );
    }
}
