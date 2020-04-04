package com.bage;

import org.hashids.Hashids;

public class Main {

    public static void main(String[] args) {
        // Encoding one number
        Hashids hashids = new Hashids("this is my salt");
        String hash = hashids.encode(12345L);
        System.out.println(hash);

        // Decoding
        long[] decode = hashids.decode(hash);
        System.out.println(decode);

    }

}
