package com.teststeps.thekla4j.utils.vavr;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.Function5;
import io.vavr.Tuple;
import io.vavr.Tuple1;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.Tuple4;
import io.vavr.Tuple5;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Either;


public class LiftEither {

  /**
   * returns a function that transforms a
   * Tuple1{Either{T}}
   * to
   * Either{Tuple1{T}}
   *
   * @param <T>
   * @return an Either lifted from the Tuple
   */
  public static <T> Function1<
    Tuple1<Either<Throwable, T>>,
    Either<Throwable, Tuple1<T>>> fromTuple1() {
    return tuple1 -> tuple1.apply(toTuple1());
  }

  public static <T> Function1<
    Either<Throwable, T>,
    Either<Throwable, Tuple1<T>>> toTuple1() {
    return u1 -> u1.map(Tuple::of);
  }

  /**
   * returns a function that transforms a
   * Tuple2{Either{T}, U}
   * to
   * Either{Tuple2{T,U}}
   *
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U> Function1<
    Tuple2<Either<Throwable, T>, U>,
    Either<Throwable, Tuple2<T, U>>> fromTuple2$1() {
    return tuple2 -> tuple2.apply(toTuple2$1());
  }

  public static <U1, U2> Function2<
    Either<Throwable, U1>,
    U2,
    Either<Throwable, Tuple2<U1, U2>>> toTuple2$1() {
    return (t1, u2) -> t1.map(u1 -> Tuple.of(u1, u2));
  }


  /**
   * returns a function that transforms a
   * Tuple2{T, Either{U}}
   * to
   * Either{Tuple2{T,U}}
   *
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U> Function1<
    Tuple2<T, Either<Throwable, U>>,
    Either<Throwable, Tuple2<T, U>>> fromTuple2$2() {
    return tuple2 -> tuple2.apply(toTuple2$2());
  }

  public static <U1, U2> Function2<
    U1,
    Either<Throwable, U2>,
    Either<Throwable, Tuple2<U1, U2>>> toTuple2$2() {
    return (u1, t2) -> t2.map(u2 -> Tuple.of(u1, u2));
  }

  /**
   * returns a function that transforms a
   * Tuple2{Either{T}, Either{U}}
   * to
   * Either{Tuple2{T,U}}
   *
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U> Function1<
    Tuple2<Either<Throwable, T>, Either<Throwable, U>>,
    Either<Throwable, Tuple2<T, U>>> fromTuple2() {
    return tuple2 -> tuple2.apply(toTuple2());
  }

  public static <U1, U2> Function2<
    Either<Throwable, U1>,
    Either<Throwable, U2>,
    Either<Throwable,
      Tuple2<U1, U2>>> toTuple2() {
    return (t1, t2) -> t1.flatMap(u1 -> t2.map(u2 -> Tuple.of(u1, u2)));
  }

  /**
   * returns a function that transforms a
   * Tuple3{Either{T}, Either{U}, Either{V}}
   * to
   * Either{Tuple3{T,U,V}}
   *
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U, V> Function1<
    Tuple3<Either<Throwable, T>, Either<Throwable, U>, Either<Throwable, V>>,
    Either<Throwable, Tuple3<T, U, V>>> fromTuple3() {

    return tuple3 -> tuple3.apply(toTuple3());
  }

  public static <U1, U2, U3> Function3<
    Either<Throwable, U1>,
    Either<Throwable, U2>,
    Either<Throwable, U3>,
    Either<Throwable, Tuple3<U1, U2, U3>>> toTuple3() {
    return (t1, t2, t3) -> t1.flatMap(u1 -> t2.flatMap(u2 -> t3.map(u3 -> Tuple.of(u1, u2, u3))));
  }


  /**
   * returns a function that transforms a
   * Tuple4{Either{T}, Either{U}, Either{V}, Either{W}}
   * to
   * Either{Tuple4{T,U,V, W}}
   *
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U, V, W> Function1<
    Tuple4<Either<Throwable, T>, Either<Throwable, U>, Either<Throwable, V>, Either<Throwable, W>>,
    Either<Throwable, Tuple4<T, U, V, W>>> fromTuple4() {
    return tuple4 -> tuple4.apply(toTuple4());
  }

  public static <U1, U2, U3, U4> Function4<
    Either<Throwable, U1>,
    Either<Throwable, U2>,
    Either<Throwable, U3>,
    Either<Throwable, U4>,
    Either<Throwable, Tuple4<U1, U2, U3, U4>>> toTuple4() {
    return (t1, t2, t3, t4) -> t1.flatMap(u1 -> t2.flatMap(u2 -> t3.flatMap(u3 -> t4.map(u4 -> Tuple.of(u1, u2, u3, u4)))));
  }

  /**
   * returns a function that transforms a
   * Tuple5{Either{T}, Either{U}, Either{V}, Either{W}, Either{X}}
   * to
   * Either{Tuple5{T,U,V, W, X}}
   *
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U, V, W, X> Function1<
    Tuple5<Either<Throwable, T>, Either<Throwable, U>, Either<Throwable, V>, Either<Throwable, W>, Either<Throwable, X>>,
    Either<Throwable, Tuple5<T, U, V, W, X>>> fromTuple5() {
    return tuple5 -> tuple5.apply(toTuple5());
  }

  public static <U1, U2, U3, U4, U5> Function5<
    Either<Throwable, U1>,
    Either<Throwable, U2>,
    Either<Throwable, U3>,
    Either<Throwable, U4>,
    Either<Throwable, U5>,
    Either<Throwable, Tuple5<U1, U2, U3, U4, U5>>> toTuple5() {
    return (t1, t2, t3, t4, t5) -> t1.flatMap(u1 -> t2.flatMap(u2 -> t3.flatMap(u3 -> t4.flatMap(u4 -> t5.map(u5 -> Tuple.of(u1, u2, u3, u4, u5))))));
  }

  /**
   * returns a function that transforms a
   * HashMap{T, Either{Throwable, U}}
   * to
   * Either{Throwable, HashMap{T, U}}
   *
   * @param <T>
   * @param <U>
   * @return Either{
   */
  public static <T, U> Function1<HashMap<T, Either<Throwable, U>>, Either<Throwable, HashMap<T, U>>> fromHashMap() {
    return hashMapE -> hashMapE.foldLeft(
      Either.<Throwable, HashMap<T, U>>right(HashMap.empty()),
      (theEither, entry) -> theEither.flatMap(newMap -> entry._2.map(value -> newMap.put(entry._1, value))));
  }

  public static <U> Function1<List<Either<Throwable, U>>, Either<Throwable, List<U>>> fromList() {
    return list -> list.foldLeft(
      Either.<Throwable, List<U>>right(List.empty()),
      (theEither, entry) -> theEither.flatMap(newList -> entry.map(newList::append)));
  }

  public static <U> Function2<Either<Throwable, List<U>>, Either<Throwable, U>, Either<Throwable, List<U>>> toList() {
    return (acc, element) -> acc.flatMap(list -> element.map(list::append));
  }
}