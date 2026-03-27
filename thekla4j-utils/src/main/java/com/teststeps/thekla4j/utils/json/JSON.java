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

/**
 * Utility class for JSON serialization and deserialization using Jackson.
 */
public class JSON {

  private static final ObjectMapper jacksonNonNullMapper =
      new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
          .registerModule(new JavaTimeModule())
          .registerModule(new SimpleModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer()))
          .registerModule(new SimpleModule().addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer()))
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .enable(SerializationFeature.INDENT_OUTPUT);


  /**
   * Serializes the given object to a JSON string, excluding null fields.
   *
   * @param <T> the type of the object
   * @param obj the object to serialize
   * @return the JSON string representation
   */
  public static <T> String jStringify(T obj) {
    try {
      return jacksonNonNullMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw JsonStringifyException.withMessageAndCause(e.getMessage(), e);
    }
  }

  /**
   * Parses the given JSON string into an instance of the specified class.
   *
   * @param <T>   the target type
   * @param json  the JSON string to parse
   * @param clazz the target class
   * @return the parsed object
   */
  public static <T> T jParse(String json, Class<T> clazz) {
    try {
      return jacksonNonNullMapper.readValue(json, clazz);
    } catch (Exception e) {
      throw JsonParseException.withMessageAndCause(e.getMessage(), e);
    }
  }

  /**
   * Returns a function that parses a JSON string into an instance of the specified class.
   *
   * @param <T>   the target type
   * @param clazz the target class
   * @return a function that parses a JSON string into the target type
   */
  public static <T> Function1<String, T> jParse(Class<T> clazz) {
    return json -> {
      try {
        return jacksonNonNullMapper.readValue(json, clazz);
      } catch (Exception e) {
        throw JsonParseException.withMessageAndCause(e.getMessage(), e);
      }
    };
  }

  /**
   * Attempts to serialize the given object to a JSON string, returning a {@link Try}.
   *
   * @param obj the object to serialize
   * @return a {@code Try} containing the JSON string, or a failure if serialization fails
   */
  public static Try<String> valueAsString(Object obj) {
    return Try.of(() -> jacksonNonNullMapper.writeValueAsString(obj));
  }

  /**
   * Attempts to parse the given JSON string into an instance of the specified class, returning a {@link Try}.
   *
   * @param <T>   the target type
   * @param str   the JSON string to parse
   * @param clazz the target class
   * @return a {@code Try} containing the parsed object, or a failure if parsing fails
   */
  public static <T> Try<T> stringToValue(String str, Class<T> clazz) {
    return Try.of(() -> jacksonNonNullMapper.readValue(str, clazz));
  }

  /**
   * Returns a log-friendly string for the given object in the format {@code ClassName {...}}.
   *
   * @param <T> the type of the object
   * @param obj the object to format
   * @return a string combining the simple class name and the JSON representation
   */
  public static <T> String logOf(T obj) {
    return obj.getClass().getSimpleName() + " " + JSON.jStringify(obj);
  }

}
