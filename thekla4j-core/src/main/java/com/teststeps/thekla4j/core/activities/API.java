package com.teststeps.thekla4j.core.activities;

import io.vavr.Function1;
import io.vavr.control.Try;

public class API {

  public static <T, R> Map<T, R> map(Function1<T, R> mapper) {
    return new Map<>(r -> Try.of(() -> mapper.apply(r)));
  }

  //  public static <T> Map<T, Void> map(Consumer<T> consumer) {
//    return new Map<>(r -> Try.run(() -> consumer.accept(r)));
//
//  }
  public static <T, R> Map<T, R> map(Function1<T, R> mapper, String reason) {
    return new Map<>(r -> Try.of(() -> mapper.apply(r)), reason);
  }

  public static <T, R> Map<T, R> mapTry(Function1<T, Try<R>> mapper) {
    return new Map<>(mapper);
  }

  public static <T, R> Map<T, R> mapTry(Function1<T, Try<R>> mapper, String reason) {
    return new Map<>(mapper, reason);
  }

}
