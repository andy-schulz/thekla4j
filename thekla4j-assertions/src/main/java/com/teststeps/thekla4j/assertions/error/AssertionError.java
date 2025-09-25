package com.teststeps.thekla4j.assertions.error;

import com.teststeps.thekla4j.commons.error.ActivityError;
import java.util.Objects;

/**
 * Specific error thrown when assertions within thekla4j activities fail.
 * <p>
 * This class extends {@link ActivityError} and can be used to indicate assertion failures
 * during test execution. It provides static factory methods for convenient instantiation
 * with or without a cause.
 */
public class AssertionError extends ActivityError {


  /**
   * Creates a new {@link AssertionError} instance from an existing {@link Throwable}.
   *
   * @param error the underlying {@link Throwable}
   * @return a new instance of {@link AssertionError}
   */
  public static AssertionError of(Throwable error) {

    if (Objects.isNull(error.getCause())) {
      return new AssertionError(error.getMessage());
    }
    return new AssertionError(error.getMessage(), error.getCause());
  }

  /**
   * Creates a new {@link AssertionError} instance from an existing {@link ActivityError}.
   *
   * @param error the underlying {@link ActivityError}
   * @return a new instance of {@link AssertionError}
   */
  public static AssertionError of(ActivityError error) {

    if (Objects.isNull(error.getCause())) {
      return new AssertionError(error.getMessage());
    }
    return new AssertionError(error.getMessage(), error.getCause());
  }


  /**
   * Creates a new {@link AssertionError} instance with an error message and a cause.
   *
   * @param message the error message
   * @param cause   the underlying {@link ActivityError}
   * @return a new instance of {@link AssertionError}
   */
  public static AssertionError of(String message, ActivityError cause) {
    return new AssertionError(message, cause);
  }

  /**
   * Creates a new {@link AssertionError} instance with an error message.
   *
   * @param message the error message
   * @return a new instance of {@link AssertionError}
   */
  public static AssertionError of(String message) {
    return new AssertionError(message);
  }

  /**
   * Constructor for an {@link AssertionError} instance with an error message.
   *
   * @param errorMessage the error message
   */
  public AssertionError(String errorMessage) {
    super(errorMessage);
  }

  /**
   * Constructor for an {@link AssertionError} instance with an error message and a cause.
   *
   * @param message the error message
   * @param cause   the underlying {@link Throwable}
   */
  public AssertionError(String message, Throwable cause) {
    super(message, cause);
  }
}
