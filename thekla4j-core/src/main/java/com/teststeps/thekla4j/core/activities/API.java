package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Activity;
import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.Tuple4;
import io.vavr.Tuple5;
import io.vavr.Tuple6;
import io.vavr.Tuple7;
import io.vavr.Tuple8;
import io.vavr.control.Try;

/**
 * Utility class to create MAP and RUN tasks
 */
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
   * @param mapper the mapper function
   * @param reason the description what the task is intended to do
   * @return the map task
   * @param <T> the input type
   * @param <R> the output type
   */
  public static <T, R> Map<T, R> map(Function1<T, R> mapper, String reason) {
    return new Map<>(r -> Try.of(() -> mapper.apply(r)), reason);
  }

  /**
   * create a map task that applies the given mapper function to the input, the mapper function returns a Try which is
   * converted to an Either
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
   * create a map task that applies the given mapper function to the input, the mapper function returns a Try which is
   * converted to an Either
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
  public static <T, R> Activity<T, R> run(Function1<T, Activity<Void, R>> runner) {
    return new Run<>(runner);
  }

  /**
   * create a task that negates the result of the given activity
   * 
   * @param activity the activity to negate
   * @return the negated activity
   * @param <T> the input type
   */
  public static <T> Activity<T, Boolean> not(Activity<T, Boolean> activity) {
    return Not.of(activity);
  }

  /**
   * combine two supplier tasks into a single supplier task that returns their results as a tuple
   * <p>
   * the tasks are run left-to-right; the run short-circuits on the first failure, otherwise the results
   * are collected into a tuple. supported for two to eight tasks.
   *
   * @param t1 the first task
   * @param t2 the second task
   * @return a supplier task returning a tuple of the two results
   * @param <R1> the result type of the first task
   * @param <R2> the result type of the second task
   */
  public static <R1, R2> SupplierTask<Tuple2<R1, R2>> zip(
                                                          SupplierTask<R1> t1, SupplierTask<R2> t2) {
    return Zip.of(t1, t2);
  }

  /**
   * combine three supplier tasks into a single supplier task that returns their results as a tuple
   *
   * @param t1 the first task
   * @param t2 the second task
   * @param t3 the third task
   * @return a supplier task returning a tuple of the three results
   * @param <R1> the result type of the first task
   * @param <R2> the result type of the second task
   * @param <R3> the result type of the third task
   */
  public static <R1, R2, R3> SupplierTask<Tuple3<R1, R2, R3>> zip(
                                                                  SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3) {
    return Zip.of(t1, t2, t3);
  }

  /**
   * combine four supplier tasks into a single supplier task that returns their results as a tuple
   *
   * @param t1 the first task
   * @param t2 the second task
   * @param t3 the third task
   * @param t4 the fourth task
   * @return a supplier task returning a tuple of the four results
   * @param <R1> the result type of the first task
   * @param <R2> the result type of the second task
   * @param <R3> the result type of the third task
   * @param <R4> the result type of the fourth task
   */
  public static <R1, R2, R3, R4> SupplierTask<Tuple4<R1, R2, R3, R4>> zip(
                                                                          SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3, SupplierTask<R4> t4) {
    return Zip.of(t1, t2, t3, t4);
  }

  /**
   * combine five supplier tasks into a single supplier task that returns their results as a tuple
   *
   * @param t1 the first task
   * @param t2 the second task
   * @param t3 the third task
   * @param t4 the fourth task
   * @param t5 the fifth task
   * @return a supplier task returning a tuple of the five results
   * @param <R1> the result type of the first task
   * @param <R2> the result type of the second task
   * @param <R3> the result type of the third task
   * @param <R4> the result type of the fourth task
   * @param <R5> the result type of the fifth task
   */
  public static <R1, R2, R3, R4, R5> SupplierTask<Tuple5<R1, R2, R3, R4, R5>> zip(
                                                                                  SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3, SupplierTask<R4> t4, SupplierTask<R5> t5) {
    return Zip.of(t1, t2, t3, t4, t5);
  }

  /**
   * combine six supplier tasks into a single supplier task that returns their results as a tuple
   *
   * @param t1 the first task
   * @param t2 the second task
   * @param t3 the third task
   * @param t4 the fourth task
   * @param t5 the fifth task
   * @param t6 the sixth task
   * @return a supplier task returning a tuple of the six results
   * @param <R1> the result type of the first task
   * @param <R2> the result type of the second task
   * @param <R3> the result type of the third task
   * @param <R4> the result type of the fourth task
   * @param <R5> the result type of the fifth task
   * @param <R6> the result type of the sixth task
   */
  public static <R1, R2, R3, R4, R5, R6> SupplierTask<Tuple6<R1, R2, R3, R4, R5, R6>> zip(
                                                                                          SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3, SupplierTask<R4> t4, SupplierTask<R5> t5, SupplierTask<R6> t6) {
    return Zip.of(t1, t2, t3, t4, t5, t6);
  }

  /**
   * combine seven supplier tasks into a single supplier task that returns their results as a tuple
   *
   * @param t1 the first task
   * @param t2 the second task
   * @param t3 the third task
   * @param t4 the fourth task
   * @param t5 the fifth task
   * @param t6 the sixth task
   * @param t7 the seventh task
   * @return a supplier task returning a tuple of the seven results
   * @param <R1> the result type of the first task
   * @param <R2> the result type of the second task
   * @param <R3> the result type of the third task
   * @param <R4> the result type of the fourth task
   * @param <R5> the result type of the fifth task
   * @param <R6> the result type of the sixth task
   * @param <R7> the result type of the seventh task
   */
  public static <R1, R2, R3, R4, R5, R6, R7> SupplierTask<Tuple7<R1, R2, R3, R4, R5, R6, R7>> zip(
                                                                                                  SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3, SupplierTask<R4> t4, SupplierTask<R5> t5, SupplierTask<R6> t6, SupplierTask<R7> t7) {
    return Zip.of(t1, t2, t3, t4, t5, t6, t7);
  }

  /**
   * combine eight supplier tasks into a single supplier task that returns their results as a tuple
   *
   * @param t1 the first task
   * @param t2 the second task
   * @param t3 the third task
   * @param t4 the fourth task
   * @param t5 the fifth task
   * @param t6 the sixth task
   * @param t7 the seventh task
   * @param t8 the eighth task
   * @return a supplier task returning a tuple of the eight results
   * @param <R1> the result type of the first task
   * @param <R2> the result type of the second task
   * @param <R3> the result type of the third task
   * @param <R4> the result type of the fourth task
   * @param <R5> the result type of the fifth task
   * @param <R6> the result type of the sixth task
   * @param <R7> the result type of the seventh task
   * @param <R8> the result type of the eighth task
   */
  public static <R1, R2, R3, R4, R5, R6, R7, R8> SupplierTask<Tuple8<R1, R2, R3, R4, R5, R6, R7, R8>> zip(
                                                                                                          SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3, SupplierTask<R4> t4, SupplierTask<R5> t5, SupplierTask<R6> t6, SupplierTask<R7> t7, SupplierTask<R8> t8) {
    return Zip.of(t1, t2, t3, t4, t5, t6, t7, t8);
  }

  private API() {
    // prevent instantiation
  }

}
