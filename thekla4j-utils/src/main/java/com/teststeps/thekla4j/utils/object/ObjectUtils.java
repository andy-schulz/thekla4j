package com.teststeps.thekla4j.utils.object;

import io.vavr.control.Try;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Utility class providing null-safe object helpers.
 */
public class ObjectUtils {

  /**
   * Safely evaluates the given supplier and returns {@code true} if the result is null
   * or if the supplier throws an exception.
   *
   * @param supplier the supplier to evaluate
   * @return {@code true} if the result is null or an exception was thrown; {@code false} otherwise
   */
  public static boolean isNullSafe(Supplier<Object> supplier) {
    return Try.of(supplier::get)
        .map(Objects::isNull)
        .getOrElse(true);
  }
}
