package com.bage.my.app.end.point.config;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.data.domain.Page;

import java.io.IOException;

// 为 Page 类型添加适配器
public class PageAdapter extends TypeAdapter<Page<?>> {
    private final Gson gson = new Gson();
    @Override
    public void write(JsonWriter out, Page<?> value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("content").jsonValue(gson.toJson(value.getContent()));
        out.name("pageable").jsonValue(gson.toJson(value.getPageable()));
        out.name("totalPages").value(value.getTotalPages());
        out.name("totalElements").value(value.getTotalElements());
        out.name("last").value(value.isLast());
        out.name("size").value(value.getSize());
        out.name("number").value(value.getNumber());
        out.name("sort").jsonValue(gson.toJson(value.getSort()));
        out.name("first").value(value.isFirst());
        out.name("numberOfElements").value(value.getNumberOfElements());
        out.name("empty").value(value.isEmpty());
        out.endObject();
    }

    @Override
    public Page<?> read(JsonReader in) throws IOException {
        // 由于 Page 是接口，我们无法直接实例化它
        // 这里我们只实现 write 方法用于序列化
        throw new UnsupportedOperationException("Cannot deserialize Page interface");
    }
}