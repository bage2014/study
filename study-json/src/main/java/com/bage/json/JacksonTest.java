package com.bage.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class JacksonTest {
    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MyBean bean = new MyBean();
        bean.setId(1);
        bean.setName("hahah");
        String result = objectMapper.writeValueAsString(bean);
        System.out.println("bean: " + result);

        String hh = objectMapper.writeValueAsString(Collections.singleton(bean));
        System.out.println("list bean: " + result);

        List<MyBean> list = objectMapper.readValue(hh, new TypeReference<List<MyBean>>() {
        });

        System.out.println("list bean[from json, java object ]: " + list.toString());

        System.out.println(list.get(0));
        System.out.println(list.get(0).getClass());

    }
}
