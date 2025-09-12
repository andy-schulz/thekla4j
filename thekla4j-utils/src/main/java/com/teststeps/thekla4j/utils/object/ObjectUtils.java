package com.teststeps.thekla4j.utils.object;

import io.vavr.control.Try;
import java.util.Objects;
import java.util.function.Supplier;

public class ObjectUtils {

  public static boolean isNullSafe(Supplier<Object> supplier) {
    return Try.of(supplier::get)
        .map(Objects::isNull)
        .getOrElse(true);
  }
}
