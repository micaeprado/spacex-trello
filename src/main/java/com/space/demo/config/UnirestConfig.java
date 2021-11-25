package com.space.demo.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class UnirestConfig {

    @PostConstruct
    public void configUnirest() {
        Unirest.setObjectMapper(new ObjectMapper() {
            final com.fasterxml.jackson.databind.ObjectMapper mapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            @SneakyThrows
            public String writeValue(Object value) {
                return mapper.writeValueAsString(value);
            }

            @SneakyThrows
            public <T> T readValue(String value, Class<T> valueType) {
                return mapper.readValue(value, valueType);
            }
        });
    }



}
