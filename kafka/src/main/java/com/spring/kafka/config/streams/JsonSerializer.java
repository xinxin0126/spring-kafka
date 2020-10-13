package com.spring.kafka.config.streams;

import java.nio.charset.Charset;
import java.util.Map;
import org.apache.kafka.common.serialization.Serializer;
import com.google.gson.Gson;

/**
 * @ClassName JsonSerializer
 * @Description
 * @Author pengzhansong
 * @Date 2020/9/22
 **/
public class JsonSerializer<T> implements Serializer<T> {

    private Gson gson = new Gson();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, T data) {
        return gson.toJson(data).getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public void close() {
    }

}
