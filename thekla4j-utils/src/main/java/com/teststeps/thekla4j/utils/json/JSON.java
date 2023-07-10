package com.teststeps.thekla4j.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vavr.Function1;

import java.lang.reflect.Type;

public class JSON {
  private static Gson nullBuilder = new GsonBuilder()
    .serializeNulls()
    .disableHtmlEscaping()
    .setPrettyPrinting()
    .excludeFieldsWithoutExposeAnnotation()
    .create();

  private static Gson nonNullBuilder = new GsonBuilder()
    .setPrettyPrinting()
    .disableHtmlEscaping()
    .excludeFieldsWithoutExposeAnnotation()
    .create();

  private static Gson objectLogger = new GsonBuilder()
    .setPrettyPrinting()
    .disableHtmlEscaping()
    .create();

  public static <T> String stringify(T obj) {
    return nonNullBuilder.toJson(obj);
  }

  public static <T> String stringifyWithNull(T obj) {
    return nullBuilder.toJson(obj);
  }

  public static <T> Function1<T, String> stringify() {
    return JSON::stringify;
  }

  public static <T> T parse(String json, Class<T> clazz) {
    return nonNullBuilder.fromJson(json, clazz);
  }

  public static <T> T parse(String json, Type typeOfT) {
    return nonNullBuilder.fromJson(json, typeOfT);
  }


  public static <T> Function1<String, T> parse(Class<T> clazz) {
    return json -> nonNullBuilder.fromJson(json, clazz);
  }

  public static <T> String logOf(T obj) {
    return obj.getClass().getSimpleName() + " " + objectLogger.toJson(obj);
  }

}
