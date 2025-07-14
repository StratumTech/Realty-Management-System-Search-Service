package com.stratumtech.realtysearch.consumer.serializer;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.commons.lang.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class DltMessageSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            log.error("Error while serializing data");
            throw new SerializationException("Error when serializing to JSON", e);
        }
    }
}
