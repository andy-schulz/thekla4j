package com.teststeps.thekla4j.utils.json.exception;

/**
 * Exception thrown when JSON parsing fails.
 */
public class JsonParseException extends RuntimeException {
  private JsonParseException(String message) {
    super(message);
  }

  private JsonParseException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new {@code JsonParseException} with the given message.
   *
   * @param message the detail message
   * @return a new exception instance
   */
  public static JsonParseException withMessage(String message) {
    return new JsonParseException(message);
  }

  /**
   * Creates a new {@code JsonParseException} with the given message and cause.
   *
   * @param message the detail message
   * @param cause   the underlying cause
   * @return a new exception instance
   */
  public static JsonParseException withMessageAndCause(String message, Throwable cause) {
    return new JsonParseException(message, cause);
  }
}
