package com.bage;

import com.google.gson.*;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GsonDatetimeTests {
    //序列化
    final static JsonSerializer<LocalDateTime> jsonSerializerDateTime = (localDateTime, type, jsonSerializationContext) -> new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    final static JsonDeserializer<LocalDateTime> jsonDeserializerDateTime = (jsonElement, type, jsonDeserializationContext) -> LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    //反序列化
    final static JsonSerializer<LocalDate> jsonSerializerDate = (localDate, type, jsonSerializationContext) -> new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    final static JsonDeserializer<LocalDate> jsonDeserializerDate = (jsonElement, type, jsonDeserializationContext) -> LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);


    @Test
    public void MyExclusionStrategy() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, jsonSerializerDateTime
                )
                .registerTypeAdapter(LocalDateTime.class, jsonDeserializerDateTime
                )

                .registerTypeAdapter(LocalDate.class, jsonSerializerDate
                )
                .registerTypeAdapter(LocalDate.class, jsonDeserializerDate
                )
                .serializeNulls()
                .create();

        Hello hello = new Hello();
        hello.setDate(LocalDate.now());
        hello.setTime(LocalDateTime.now());
        String json = gson.toJson(hello);
        System.out.println(json);

        Hello hello1 = gson.fromJson(json, Hello.class);

        System.out.println(hello1);
    }


}


class Hello {
    LocalDateTime time;
    LocalDate date;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}

