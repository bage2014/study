package com.bage.study.podam;

import com.google.gson.GsonBuilder;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


public class HelloPodamTests {
    public static void main(String[] args) {
        PodamFactory factory = new PodamFactoryImpl();
        UserModel userModel = factory.manufacturePojo(UserModel.class);
        System.out.println(new GsonBuilder ().create().toJson(userModel));
    }
}
