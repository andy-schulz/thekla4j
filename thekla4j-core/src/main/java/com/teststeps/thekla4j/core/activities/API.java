package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.core.base.activities.Activity;
import io.vavr.Function1;
import io.vavr.control.Try;

public class API {

  /**
   * create a map task that applies the given mapper function to the input
   *
   * @param mapper the mapper function
   * @return the map task
   * @param <T> the input type
   * @param <R> the output type
   */
  public static <T, R> Map<T, R> map(Function1<T, R> mapper) {
    return new Map<>(r -> Try.of(() -> mapper.apply(r)));
  }

  /**
   * create a map task that applies the given mapper function to the input and passes the given reason to the task
   * the reason is used in the activity log to describe the task
   *
   * @param mapper  the mapper function
   * @param reason the description what the task is intended to do
   * @return the map task
   * @param <T> the input type
   * @param <R> the output type
   */
  public static <T, R> Map<T, R> map(Function1<T, R> mapper, String reason) {
    return new Map<>(r -> Try.of(() -> mapper.apply(r)), reason);
  }

  /**
   * create a map task that applies the given mapper function to the input, the mapper function returns a Try which is converted to an Either
   * when the task is executed and fails, the error is logged in the activity log
   *
   * @param mapper the mapper function
   * @return the map task
   * @param <T> the input type
   * @param <R> the output type
   */
  public static <T, R> Map<T, R> mapTry(Function1<T, Try<R>> mapper) {
    return new Map<>(mapper);
  }

  /**
   * create a map task that applies the given mapper function to the input, the mapper function returns a Try which is converted to an Either
   * when the task is executed and fails, the error is logged in the activity log
   * the reason is used in the activity log to describe the task
   *
   * @param mapper the mapper function
   * @param reason the description what the task is intended to do
   * @return the map task
   * @param <T> the input type
   * @param <R> the output type
   */
  public static <T, R> Map<T, R> mapTry(Function1<T, Try<R>> mapper, String reason) {
    return new Map<>(mapper, reason);
  }

  /**
   * create a runnable task that creates n new activity
   *
   * @param runner the function that creates the new activity
   * @return the run task
   * @param <T> the input type
   * @param <R> the output type
   */
  public static <T,R> Activity<T,R> run(Function1<T, Activity<Void,R>> runner) {
    return new Run<>(runner);
  }

  private API() {
    // prevent instantiation
  }

}
