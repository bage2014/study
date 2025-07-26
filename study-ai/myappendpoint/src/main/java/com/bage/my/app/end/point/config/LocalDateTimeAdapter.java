package com.bage.my.app.end.point.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    // 使用DateTimeFormatterBuilder构建更灵活的日期时间格式解析器
    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-")
            .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2,  java.time.format.SignStyle.NEVER)
            .appendPattern("-")
            .appendValue(ChronoField.DAY_OF_MONTH, 1, 2,  java.time.format.SignStyle.NEVER)
            .appendPattern(" ")
            .appendValue(ChronoField.HOUR_OF_DAY, 1, 2,  java.time.format.SignStyle.NEVER)
            .appendPattern(":")
            .appendValue(ChronoField.MINUTE_OF_HOUR, 1, 2,  java.time.format.SignStyle.NEVER)
            .appendPattern(":")
            .appendValue(ChronoField.SECOND_OF_MINUTE, 1, 2,  java.time.format.SignStyle.NEVER)
            .toFormatter();

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        // 输出仍保持为标准格式 "yyyy-MM-dd HH:mm:ss"
        out.value(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        String dateTimeString = in.nextString();
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}