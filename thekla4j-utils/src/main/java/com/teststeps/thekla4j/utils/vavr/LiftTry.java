package com.teststeps.thekla4j.utils.vavr;

import io.vavr.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class LiftTry {

  /**
   * transforming a Try{R} into an Either{L,R}
   *
   * @param toErrorFunction Either{L,R}
   * @return Either{L,R}
   */
  public static <L, R> Function1<Try<R>, Either<L, R>> toEither(Function1<Throwable, L> toErrorFunction) {
    return tryObj -> tryObj.isFailure() ? Either.left(toErrorFunction.apply(tryObj.getCause())) :
        Either.right(tryObj.get());
  }

  /**
   * transforming an Option{R} into an Either{L,R}
   *
   * @param errorMessage in case the Option is empty create a failing Try with this error message
   * @return Either{L,R}
   */
  public static <R> Function1<Option<R>, Try<R>> fromOption(String errorMessage) {
    return option -> option.isEmpty() ? Try.failure(new Throwable(errorMessage)) :
        Try.success(option.get());
  }


  /**
   * returns a function that transforms a
   * Tuple2{Try{T}, Try{U}}
   * to
   * Try{Tuple{T,U}}
   *
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U> Function1<Tuple2<Try<T>, Try<U>>, Try<Tuple2<T, U>>> fromTuple2() {
    return tuple2 -> tuple2._1().map(t -> Tuple.of(t, tuple2._2))
                           .flatMap(t -> tuple2._2.map(u -> Tuple.of(t._1, u)));
  }


  public static <U1, U2> Function2<Try<U1>, Try<U2>, Try<Tuple2<U1, U2>>> toTuple2() {
    return (t1, t2) -> t1.flatMap((u1) -> t2.map((u2) -> Tuple.of(u1, u2)));
  }

  /**
   * returns a function that transforms a
   * Tuple2{Try{T}, U}
   * to
   * Try{Tuple{T,U}}
   *
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U> Function1<Tuple2<Try<T>, U>, Try<Tuple2<T, U>>> fromTuple2$1() {
    return tuple2 -> tuple2._1().map(t -> Tuple.of(t, tuple2._2));
  }


  /**
   * returns a function that transforms a
   * Tuple2{T, Try{U}}
   * to
   * Try{Tuple{T,U}}
   *
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U> Function1<Tuple2<T, Try<U>>, Try<Tuple2<T, U>>> fromTuple2$2() {
    return tuple2 -> tuple2._2.map(u -> Tuple.of(tuple2._1, u));
  }


  public static <T, U> Function2<T, Try<U>, Try<Tuple2<T, U>>> fromInnerTuple2$2() {
    return (t1, t2) -> t2.map(u -> Tuple.of(t1, u));
  }

  /**
   * returns a function that transforms a
   * Tuple3{Try{T}, Try{U}, Try{V}}
   * to
   * Try{Tuple{T,U,V}}
   *
   * @param <T>
   * @param <U>
   * @param <V>
   * @return
   */
  public static <T, U, V> Function1<Tuple3<Try<T>, Try<U>, Try<V>>, Try<Tuple3<T, U, V>>> fromTuple3() {
    return tuple3 -> tuple3._1().map(t -> Tuple.of(t, tuple3._2, tuple3._3))
                           .flatMap(t -> tuple3._2.map(u -> Tuple.of(t._1, u, t._3)))
                           .flatMap(t -> tuple3._3.map(v -> Tuple.of(t._1, t._2, v)));
  }

  public static <U1, U2, U3> Function3<Try<U1>, Try<U2>, Try<U3>, Try<Tuple3<U1, U2, U3>>> toTuple3() {
    return (t1, t2, t3) -> t1.flatMap(u1 -> t2.flatMap(u2 -> t3.map(u3 -> Tuple.of(u1, u2, u3))));
  }

  /**
   * returns a function that transforms a
   * Tuple3{Try{T}, U, V}
   * to
   * Try{Tuple{T,U,V}}
   *
   * @param <T>
   * @param <U>
   * @param <V>
   * @return
   */
  public static <T, U, V> Function1<Tuple3<Try<T>, U, V>, Try<Tuple3<T, U, V>>> fromTuple3$1() {
    return tuple3 -> tuple3._1().map(t -> Tuple.of(t, tuple3._2, tuple3._3));
  }

  /**
   * returns a function that transforms a
   * Tuple3{T, Try{U}, V}
   * to
   * Try{Tuple{T,U,V}}
   *
   * @param <T>
   * @param <U>
   * @param <V>
   * @return
   */
  public static <T, U, V> Function1<Tuple3<T, Try<U>, V>, Try<Tuple3<T, U, V>>> fromTuple3$2() {
    return tuple3 -> tuple3._2().map(t -> Tuple.of(tuple3._1, t, tuple3._3));
  }

  /**
   * returns a function that transforms a
   * Tuple3{T, U, Try{V}}
   * to
   * Try{Tuple{T,U,V}}
   *
   * @param <T>
   * @param <U>
   * @param <V>
   * @return
   */
  public static <T, U, V> Function1<Tuple3<T, U, Try<V>>, Try<Tuple3<T, U, V>>> fromTuple3$3() {
    return tuple3 -> tuple3._3().map(t -> Tuple.of(tuple3._1, tuple3._2, t));
  }

  /**
   * returns a function that transforms a
   * Tuple3{T, Try{U}, Try{V}}
   * to
   * Try{Tuple{T,U,V}}
   *
   * @param <T>
   * @param <U>
   * @param <V>
   * @return
   */
  public static <T, U, V> Function1<Tuple3<T, Try<U>, Try<V>>, Try<Tuple3<T, U, V>>> fromTuple3$2$3() {
    return tuple3 -> tuple3._2()
                           .map(t -> Tuple.of(tuple3._1, t, tuple3._3))
                           .flatMap(t -> tuple3._3.map(v -> Tuple.of(t._1, t._2, v)));
  }


  /**
   * returns a function that transforms a
   * Tuple4{Try{T}, U, V, W}
   * to
   * Try{Tuple4{T,U,V,W}}
   */
  public static <T, U, V, W> Function1<Tuple4<Try<T>, U, V, W>, Try<Tuple4<T, U, V, W>>> fromTuple4$1() {
    return tuple4 -> tuple4._1().map(t -> Tuple.of(t, tuple4._2, tuple4._3, tuple4._4));
  }

  /**
   * returns a function that transforms a
   * Tuple4{T, U, Try{V}, W}
   * to
   * Try{Tuple4{T,U,V,W}}
   */
  public static <T, U, V, W> Function1<Tuple4<T, U, Try<V>, W>, Try<Tuple4<T, U, V, W>>> fromTuple4$3() {
    return tuple4 -> tuple4._3().map(v -> Tuple.of(tuple4._1, tuple4._2, v, tuple4._4));
  }

  /**
   * returns a function that transforms a
   * Tuple4{Try{T}, U, Try{V}, W}
   * to
   * Try{Tuple4{T,U,V,W}}
   */
  public static <T, U, V, W> Function1<Tuple4<Try<T>, U, Try<V>, W>, Try<Tuple4<T, U, V, W>>> fromTuple4$1$3() {
    return tuple4 -> LiftTry.<T, U, Try<V>, W>fromTuple4$1().apply(tuple4)
                            .flatMap(LiftTry.fromTuple4$3());

  }


  public static <S, T> Function1<HashMap<S, Try<T>>, Try<HashMap<S, T>>> fromHashMapValue() {
    return hashMap -> hashMap.foldLeft(
      Try.success(HashMap.empty()),
      (theTry, entry) -> theTry.flatMap(newMap -> entry._2.map(value -> newMap.put(entry._1, value))));
  }

  public static <S, T> Function1<HashMap<Try<S>, T>, Try<HashMap<S, T>>> fromHashMapKey() {
    return hashMap -> hashMap.foldLeft(
      Try.success(HashMap.empty()),
      (theTry, entry) -> theTry.flatMap(newMap -> entry._1.map(key -> newMap.put(key, entry._2))));
  }


  public static <S,T> Function1<HashMap<Try<S>, Try<T>>, Try<HashMap<S,T>>> fromHashMap() {
    return hashMap -> hashMap.foldLeft(
      Try.success(HashMap.empty()),
      (theTry, entry) -> theTry.flatMap(accMap -> entry._1.flatMap(key -> entry._2.map(value -> accMap.put(key, value)))));
  }


  public static <S, T> Function1<Map<S, Try<T>>, Try<Map<S, T>>> fromMap() {
    return map -> map.foldLeft(
        // have to remove all entries from the map as I don't know which type of map it is (e.g. HashMap, TreeMap, ...)
        // values mapping has to be done so that the return types match, it has no effect as the map is already empty
        Try.success(map.removeAll(map.keySet()).mapValues(Try::get)),
        (theTry, entry) -> theTry.flatMap(newMap -> entry._2.map(value -> newMap.put(entry._1, value))));
  }

  public static <T> Function1<List<Try<T>>, Try<List<T>>> fromList() {
    return list -> list.foldLeft(
      Try.success(List.empty()), LiftTry.fromListMapper());
  }

  public static <T> Function2<Try<List<T>>, Try<T>, Try<List<T>>> fromListMapper() {
    return (theTry, tryElement) -> theTry.flatMap(newList -> tryElement.map(newList::append));
  }


  public static <T> Function1<Seq<Try<T>>, Try<Seq<T>>> fromSeq() {
    return seq -> seq.foldLeft(
      Try.success(List.empty()),
      (theTry, entry) -> theTry.flatMap(newList -> entry.map(newList::append)));
  }
}
