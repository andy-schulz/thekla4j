package com.teststeps.thekla4j.utils.json.exception;

public class JsonParseException extends RuntimeException {
  private JsonParseException(String message) {
    super(message);
  }

  private JsonParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public static JsonParseException withMessage(String message) {
    return new JsonParseException(message);
  }

  public static JsonParseException withMessageAndCause(String message, Throwable cause) {
    return new JsonParseException(message, cause);
  }
}
