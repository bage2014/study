package com.bage.study.springboot.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ObjectJsonSerializer {

    public static class Serializer extends JsonSerializer<HelloJsonModel> {

        @Override
        public void serialize(HelloJsonModel aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(String.valueOf(aLong));
        }
    }

    public static class Deserializer extends JsonDeserializer<HelloJsonModel> {


        @Override
        public HelloJsonModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            String text = jsonParser.getText();
            return new HelloJsonModel();
        }
    }

}
