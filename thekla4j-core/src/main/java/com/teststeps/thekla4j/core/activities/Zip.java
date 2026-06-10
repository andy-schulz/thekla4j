package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.Tuple4;
import io.vavr.Tuple5;
import io.vavr.Tuple6;
import io.vavr.Tuple7;
import io.vavr.Tuple8;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * A supplier task that runs a list of source supplier tasks and combines their results into a single value.
 * <p>
 * The tasks are run left-to-right. The run short-circuits on the first failure (Left), otherwise the ordered
 * results are passed to the combiner to build the resulting value (typically a Vavr Tuple).
 *
 * @param <R> the combined result type
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Workflow("collect the results of the supplied tasks into a tuple")
public class Zip<R> extends SupplierTask<R> {

  private final List<SupplierTask<?>> tasks;
  private final Function1<List<Object>, R> combiner;

  @Override
  protected Either<ActivityError, R> performAs(Actor actor) {
    return tasks
        .foldLeft(
          Either.<ActivityError, List<Object>>right(List.empty()),
          (acc, task) -> acc.flatMap(rs -> task.runAs(actor).map(rs::append)))
        .map(combiner);
  }

  /**
   * combine two supplier tasks into a single supplier task that returns their results as a tuple
   *
   * @param t1 the first task
   * @param t2 the second task
   * @return a supplier task returning a tuple of the two results
   * @param <R1> the result type of the first task
   * @param <R2> the result type of the second task
   */
  @SuppressWarnings("unchecked")
  public static <R1, R2> Zip<Tuple2<R1, R2>> of(
                                                SupplierTask<R1> t1, SupplierTask<R2> t2) {
    return new Zip<>(List.of(t1, t2),
                     r -> Tuple.of((R1) r.get(0), (R2) r.get(1)));
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
  @SuppressWarnings("unchecked")
  public static <R1, R2, R3> Zip<Tuple3<R1, R2, R3>> of(
                                                        SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3) {
    return new Zip<>(List.of(t1, t2, t3),
                     r -> Tuple.of((R1) r.get(0), (R2) r.get(1), (R3) r.get(2)));
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
  @SuppressWarnings("unchecked")
  public static <R1, R2, R3, R4> Zip<Tuple4<R1, R2, R3, R4>> of(
                                                                SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3, SupplierTask<R4> t4) {
    return new Zip<>(List.of(t1, t2, t3, t4),
                     r -> Tuple.of((R1) r.get(0), (R2) r.get(1), (R3) r.get(2), (R4) r.get(3)));
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
  @SuppressWarnings("unchecked")
  public static <R1, R2, R3, R4, R5> Zip<Tuple5<R1, R2, R3, R4, R5>> of(
                                                                        SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3, SupplierTask<R4> t4, SupplierTask<R5> t5) {
    return new Zip<>(List.of(t1, t2, t3, t4, t5),
                     r -> Tuple.of((R1) r.get(0), (R2) r.get(1), (R3) r.get(2), (R4) r.get(3), (R5) r.get(4)));
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
  @SuppressWarnings("unchecked")
  public static <R1, R2, R3, R4, R5, R6> Zip<Tuple6<R1, R2, R3, R4, R5, R6>> of(
                                                                                SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3, SupplierTask<R4> t4, SupplierTask<R5> t5, SupplierTask<R6> t6) {
    return new Zip<>(List.of(t1, t2, t3, t4, t5, t6),
                     r -> Tuple.of((R1) r.get(0), (R2) r.get(1), (R3) r.get(2), (R4) r.get(3), (R5) r.get(4), (R6) r.get(5)));
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
  @SuppressWarnings("unchecked")
  public static <R1, R2, R3, R4, R5, R6, R7> Zip<Tuple7<R1, R2, R3, R4, R5, R6, R7>> of(
                                                                                        SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3, SupplierTask<R4> t4, SupplierTask<R5> t5, SupplierTask<R6> t6, SupplierTask<R7> t7) {
    return new Zip<>(List.of(t1, t2, t3, t4, t5, t6, t7),
                     r -> Tuple.of((R1) r.get(0), (R2) r.get(1), (R3) r.get(2), (R4) r.get(3), (R5) r.get(4), (R6) r.get(5), (R7) r.get(6)));
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
  @SuppressWarnings("unchecked")
  public static <R1, R2, R3, R4, R5, R6, R7, R8> Zip<Tuple8<R1, R2, R3, R4, R5, R6, R7, R8>> of(
                                                                                                SupplierTask<R1> t1, SupplierTask<R2> t2, SupplierTask<R3> t3, SupplierTask<R4> t4, SupplierTask<R5> t5, SupplierTask<R6> t6, SupplierTask<R7> t7, SupplierTask<R8> t8) {
    return new Zip<>(List.of(t1, t2, t3, t4, t5, t6, t7, t8),
                     r -> Tuple.of((R1) r.get(0), (R2) r.get(1), (R3) r.get(2), (R4) r.get(3), (R5) r.get(4), (R6) r.get(5), (R7) r.get(6), (R8) r
                         .get(7)));
  }
}
