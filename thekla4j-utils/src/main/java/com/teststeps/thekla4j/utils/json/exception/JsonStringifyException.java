package com.teststeps.thekla4j.utils.json.exception;

public class JsonStringifyException extends RuntimeException {

  private JsonStringifyException(String message) {
    super(message);
  }

  private JsonStringifyException(String message, Throwable cause) {
    super(message, cause);
  }

  public static JsonStringifyException withMessage(String message) {
    return new JsonStringifyException(message);
  }

  public static JsonStringifyException withMessageAndCause(String message, Throwable cause) {
    return new JsonStringifyException(message, cause);
  }
}
