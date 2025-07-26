package com.bage.my.app.end.point.config;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public class PageableAdapter extends TypeAdapter<Pageable> {
        private final Gson gson = new Gson();
        @Override
        public void write(JsonWriter out, Pageable value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginObject();
            out.name("pageNumber").value(value.getPageNumber());
            out.name("pageSize").value(value.getPageSize());
            out.name("offset").value(value.getOffset());
            out.name("paged").value(value.isPaged());
            out.name("unpaged").value(value.isUnpaged());
            out.name("sort").jsonValue(gson.toJson(value.getSort()));
            out.endObject();
        }

        @Override
        public Pageable read(JsonReader in) throws IOException {
            // 由于 Pageable 是接口，我们无法直接实例化它
            // 这里我们只实现 write 方法用于序列化
            throw new UnsupportedOperationException("Cannot deserialize Pageable interface");
        }
    }
