package com.bage.my.app.end.point.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.data.domain.Sort;

import java.io.IOException;

public class SortAdapter extends TypeAdapter<Sort> {

    @Override
    public void write(JsonWriter out, Sort value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("empty").value(value.isEmpty());
        out.name("sorted").value(value.isSorted());
        out.name("unsorted").value(value.isUnsorted());
        out.endObject();
    }

    @Override
    public Sort read(JsonReader in) throws IOException {
        // 由于 Sort 是接口，我们无法直接实例化它
        // 这里我们只实现 write 方法用于序列化
        throw new UnsupportedOperationException("Cannot deserialize Sort interface");
    }
}
