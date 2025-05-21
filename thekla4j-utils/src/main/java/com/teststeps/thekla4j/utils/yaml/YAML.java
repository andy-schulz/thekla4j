package com.teststeps.thekla4j.utils.yaml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.teststeps.thekla4j.utils.json.exception.JsonStringifyException;
import com.teststeps.thekla4j.utils.json.serialization.LocalDateTimeDeserializer;
import com.teststeps.thekla4j.utils.json.serialization.LocalDateTimeSerializer;
import io.vavr.Function1;
import io.vavr.control.Try;
import io.vavr.jackson.datatype.VavrModule;
import java.time.LocalDateTime;

public class YAML {
  private static final ObjectMapper yamlNonNullMapper =
      new YAMLMapper()
          .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .setSerializationInclusion(JsonInclude.Include.NON_NULL)
          .registerModule(new VavrModule())
          .registerModule(new JavaTimeModule())
          .registerModule(new SimpleModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer()))
          .registerModule(new SimpleModule().addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer()))
          .enable(SerializationFeature.INDENT_OUTPUT);


  public static <T> String jStringify(T obj) {
    try {
      return yamlNonNullMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw JsonStringifyException.withMessageAndCause(e.getMessage(), e);
    }
  }

  public static <T> Try<T> jParse(String json, Class<T> clazz) {
    return Try.of(() -> yamlNonNullMapper.readValue(json, clazz));
  }

  public static <T> Function1<String, Try<T>> jParse(Class<T> clazz) {
    return json -> Try.of(() -> yamlNonNullMapper.readValue(json, clazz));
  }
}
