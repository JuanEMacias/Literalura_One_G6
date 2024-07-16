package com.challenge.literalura.service;

public interface ConversionDatos {

    <T> T convertData(String data, Class<T> classType);
}
