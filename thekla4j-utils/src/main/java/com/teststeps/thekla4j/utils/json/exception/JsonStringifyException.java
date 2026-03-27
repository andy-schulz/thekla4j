package com.teststeps.thekla4j.utils.json.exception;

/**
 * Exception thrown when JSON serialization fails.
 */
public class JsonStringifyException extends RuntimeException {

  private JsonStringifyException(String message) {
    super(message);
  }

  private JsonStringifyException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new {@code JsonStringifyException} with the given message.
   *
   * @param message the detail message
   * @return a new exception instance
   */
  public static JsonStringifyException withMessage(String message) {
    return new JsonStringifyException(message);
  }

  /**
   * Creates a new {@code JsonStringifyException} with the given message and cause.
   *
   * @param message the detail message
   * @param cause   the underlying cause
   * @return a new exception instance
   */
  public static JsonStringifyException withMessageAndCause(String message, Throwable cause) {
    return new JsonStringifyException(message, cause);
  }
}
