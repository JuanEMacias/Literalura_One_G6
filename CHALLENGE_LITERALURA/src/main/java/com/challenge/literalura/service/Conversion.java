package com.challenge.literalura.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Conversion implements ConversionDatos {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T convertData(String data, Class<T> classType) {
        try {
            return objectMapper.readValue(data, classType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
