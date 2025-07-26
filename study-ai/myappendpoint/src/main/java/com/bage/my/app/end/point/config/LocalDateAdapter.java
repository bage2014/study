package com.bage.my.app.end.point.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    // 使用DateTimeFormatterBuilder构建更灵活的日期格式解析器
    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-")
            .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, java.time.format.SignStyle.NEVER)
            .appendPattern("-")
            .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, java.time.format.SignStyle.NEVER)
            .toFormatter();

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        // 输出仍保持为标准格式 "yyyy-MM-dd"
        out.value(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        String dateString = in.nextString();
        log.info("read LocalDate: {}", dateString);
        return LocalDate.parse(dateString, formatter);
    }
}