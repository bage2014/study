package com.bage;

import com.bage.json.MyExclusionStrategy;
import com.bage.json.SampleObjectForTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

public class GsonTests {


    @Test
    public void MyExclusionStrategy (){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new MyExclusionStrategy(String.class))
                .serializeNulls()
                .create();
        SampleObjectForTest src = new SampleObjectForTest();
        String json = gson.toJson(src);
        System.out.println(json);
    }

    @Test
    public void nullSupport (){
        Gson gson = new GsonBuilder().serializeNulls().create();

        BagOfPrimitives obj = new BagOfPrimitives();
        obj.setValue2(null);
        System.out.println(obj);

        System.out.println(gson.toJson(obj));
    }
    @Test
    public void Collections (){
        Gson gson = new Gson();

        Collection<Integer> ints = Arrays.asList(1,2,3,4,5);

// Serialization
        String json = gson.toJson(ints);  // ==> json is [1,2,3,4,5]

        System.out.println(json);
// Deserialization
        Type collectionType = new TypeToken<Collection<Integer>>(){}.getType();
        Collection<Integer> ints2 = gson.fromJson(json, collectionType);
// ==> ints2 is same as ints
        System.out.println(ints2);


    }
    @Test
    public void Object(){
        // Serialization
        BagOfPrimitives obj = new BagOfPrimitives();
        System.out.println(obj);
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        // ==> json is {"value1":1,"value2":"abc"}
        System.out.println(json);

        // Deserialization
        BagOfPrimitives obj2 = gson.fromJson(json, BagOfPrimitives.class);
        // ==> obj2 is just like obj
        System.out.println(obj2);
    }

    @Test
    public void Primitives(){
        // Serialization
        Gson gson = new Gson();


        System.out.println(gson.toJson(1));            // ==> 1
        System.out.println(gson.toJson("abcd"));      // ==> "abcd"
        System.out.println(gson.toJson(new Long(10))); // ==> 10
        int[] values = { 1 };
        System.out.println(gson.toJson(values));       // ==> [1]

        // Deserialization
        int one = gson.fromJson("1", int.class);
        Integer oneI = gson.fromJson("1", Integer.class);
        Long oneL = gson.fromJson("1", Long.class);
        Boolean fls = gson.fromJson("false", Boolean.class);
        String str = gson.fromJson("\"abc\"", String.class);
        String[] anotherStr = gson.fromJson("[\"abc\"]", String[].class);


        System.out.println("one:" + one);
        System.out.println("oneI:" + oneI);
        System.out.println("oneL:" + oneL);
        System.out.println("fls:" + fls);
        System.out.println("str:" + str);
        System.out.println("anotherStr:" + anotherStr);
    }



}

class BagOfPrimitives {
    private int value1 = 1;
    private String value2 = "abc";
    private transient int value3 = 3;
    BagOfPrimitives() {
        // no-args constructor
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    @Override
    public String toString() {
        return "BagOfPrimitives{" +
                "value1=" + value1 +
                ", value2='" + value2 + '\'' +
                ", value3=" + value3 +
                '}';
    }
}
