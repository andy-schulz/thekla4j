package com.teststeps.thekla4j.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.teststeps.thekla4j.utils.json.exception.JsonParseException;
import com.teststeps.thekla4j.utils.json.exception.JsonStringifyException;
import com.teststeps.thekla4j.utils.json.serialization.LocalDateTimeDeserializer;
import com.teststeps.thekla4j.utils.json.serialization.LocalDateTimeSerializer;
import io.vavr.Function1;
import io.vavr.control.Try;
import java.time.LocalDateTime;

public class JSON {

  private static final ObjectMapper jacksonNonNullMapper =
      new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
          .registerModule(new JavaTimeModule())
          .registerModule(new SimpleModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer()))
          .registerModule(new SimpleModule().addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer()))
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .enable(SerializationFeature.INDENT_OUTPUT);


  public static <T> String jStringify(T obj) {
    try {
      return jacksonNonNullMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw JsonStringifyException.withMessageAndCause(e.getMessage(), e);
    }
  }

  public static <T> T jParse(String json, Class<T> clazz) {
    try {
      return jacksonNonNullMapper.readValue(json, clazz);
    } catch (Exception e) {
      throw JsonParseException.withMessageAndCause(e.getMessage(), e);
    }
  }

  public static <T> Function1<String, T> jParse(Class<T> clazz) {
    return json -> {
      try {
        return jacksonNonNullMapper.readValue(json, clazz);
      } catch (Exception e) {
        throw JsonParseException.withMessageAndCause(e.getMessage(), e);
      }
    };
  }

  public static Try<String> valueAsString(Object obj) {
    return Try.of(() -> jacksonNonNullMapper.writeValueAsString(obj));
  }

  public static <T> Try<T> stringToValue(String str, Class<T> clazz) {
    return Try.of(() -> jacksonNonNullMapper.readValue(str, clazz));
  }

  public static <T> String logOf(T obj) {
    return obj.getClass().getSimpleName() + " " + JSON.jStringify(obj);
  }

}
