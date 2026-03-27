package com.teststeps.thekla4j.utils.vavr;

import io.vavr.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;

/**
 * Utility class providing functions to lift {@link io.vavr.control.Try} values
 * out of tuples, options, maps, and sequences.
 */
public class LiftTry {


  /**
   * returns a function that lifts the Try from an Option
   * <p>
   * Option{Try{T}} -> Try{Option{T}}
   *
   * @return the function that lifts the Try from an Option
   */
  /**
   * returns a function that lifts the Try from an Option
   * <p>
   * Option{Try{T}} -> Try{Option{T}}
   *
   * @param <T> the success type wrapped inside the Try
   * @return the function that lifts the Try from an Option
   */
  public static <T> Function1<Option<Try<T>>, Try<Option<T>>> fromOption() {
    return option -> option.isEmpty() ? Try.success(Option.none()) : option.get().map(Option::some);
  }

  /**
   * returns a function that lifts the trys from a tuple
   * <p>
   * Tuple2{Try{T}, Try{U}} -> Try{Tuple{T,U}}
   *
   * @param <T> the success type of the first Try
   * @param <U> the success type of the second Try
   * @return the function that transforms the tuple
   */
  public static <T, U> Function1<Tuple2<Try<T>, Try<U>>, Try<Tuple2<T, U>>> fromTuple2() {
    return tuple2 -> tuple2._1()
        .map(t -> Tuple.of(t, tuple2._2))
        .flatMap(t -> tuple2._2.map(u -> Tuple.of(t._1, u)));
  }


  /**
   * returns a function that transforms two
   * Try{U1} and Try{U2}
   * to
   * Try{Tuple{U1,U2}}
   *
   * @param <U1> the success type of the first Try
   * @param <U2> the success type of the second Try
   * @return the function that transforms into a tuple
   */
  public static <U1, U2> Function2<Try<U1>, Try<U2>, Try<Tuple2<U1, U2>>> toTuple2() {
    return (t1, t2) -> t1.flatMap((u1) -> t2.map((u2) -> Tuple.of(u1, u2)));
  }

  /**
   * returns a function that transforms a
   * Tuple2{Try{T}, U}
   * to
   * Try{Tuple{T,U}}
   *
   * @param <T> the success type wrapped in the first Try element
   * @param <U> the type of the plain second element
   * @return the function that transforms the tuple
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
   * @param <T> the type of the plain first element
   * @param <U> the success type wrapped in the second Try element
   * @return the function that transforms the tuple
   */
  public static <T, U> Function1<Tuple2<T, Try<U>>, Try<Tuple2<T, U>>> fromTuple2$2() {
    return tuple2 -> tuple2._2.map(u -> Tuple.of(tuple2._1, u));
  }


  /**
   * returns a function that combines T and Try{U} into Try{Tuple2{T,U}}
   *
   * @param <T> the type of the plain first value
   * @param <U> the success type of the second Try
   * @return a function that maps the Try value into a Tuple2
   */
  public static <T, U> Function2<T, Try<U>, Try<Tuple2<T, U>>> fromInnerTuple2$2() {
    return (t1, t2) -> t2.map(u -> Tuple.of(t1, u));
  }

  /**
   * returns a function that transforms a
   * Tuple3{Try{T}, Try{U}, Try{V}}
   * to
   * Try{Tuple{T,U,V}}
   *
   * @param <T> the success type of the first Try
   * @param <U> the success type of the second Try
   * @param <V> the success type of the third Try
   * @return the function that transforms the tuple
   */
  public static <T, U, V> Function1<Tuple3<Try<T>, Try<U>, Try<V>>, Try<Tuple3<T, U, V>>> fromTuple3() {
    return tuple3 -> tuple3._1()
        .map(t -> Tuple.of(t, tuple3._2, tuple3._3))
        .flatMap(t -> tuple3._2.map(u -> Tuple.of(t._1, u, t._3)))
        .flatMap(t -> tuple3._3.map(v -> Tuple.of(t._1, t._2, v)));
  }

  /**
   * returns a function that combines Try{U1}, Try{U2} and Try{U3} into Try{Tuple3{U1,U2,U3}}
   *
   * @param <U1> the success type of the first Try
   * @param <U2> the success type of the second Try
   * @param <U3> the success type of the third Try
   * @return a function that flatMaps all three Try values into a single Try of a Tuple3
   */
  public static <U1, U2, U3> Function3<Try<U1>, Try<U2>, Try<U3>, Try<Tuple3<U1, U2, U3>>> toTuple3() {
    return (t1, t2, t3) -> t1.flatMap(u1 -> t2.flatMap(u2 -> t3.map(u3 -> Tuple.of(u1, u2, u3))));
  }

  /**
   * returns a function that transforms a
   * Tuple3{Try{T}, U, V}
   * to
   * Try{Tuple{T,U,V}}
   *
   * @param <T> the success type wrapped in the first Try element
   * @param <U> the type of the plain second element
   * @param <V> the type of the plain third element
   * @return the function that lifts the Try out of the first tuple element
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
   * @param <T> the type of the plain first element
   * @param <U> the success type wrapped in the second Try element
   * @param <V> the type of the plain third element
   * @return the function that lifts the Try out of the second tuple element
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
   * @param <T> the type of the plain first element
   * @param <U> the type of the plain second element
   * @param <V> the success type wrapped in the third Try element
   * @return the function that lifts the Try out of the third tuple element
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
   * @param <T> the type of the plain first element
   * @param <U> the success type wrapped in the second Try element
   * @param <V> the success type wrapped in the third Try element
   * @return the function that lifts both Try values out of the tuple elements
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
   *
   * @param <T> the success type wrapped in the first Try element
   * @param <U> the type of the plain second element
   * @param <V> the type of the plain third element
   * @param <W> the type of the plain fourth element
   * @return a function that lifts the Try out of the first tuple element
   */
  public static <T, U, V, W> Function1<Tuple4<Try<T>, U, V, W>, Try<Tuple4<T, U, V, W>>> fromTuple4$1() {
    return tuple4 -> tuple4._1().map(t -> Tuple.of(t, tuple4._2, tuple4._3, tuple4._4));
  }

  /**
   * returns a function that transforms a
   * Tuple4{T, U, Try{V}, W}
   * to
   * Try{Tuple4{T,U,V,W}}
   *
   * @param <T> the type of the plain first element
   * @param <U> the type of the plain second element
   * @param <V> the success type wrapped in the third Try element
   * @param <W> the type of the plain fourth element
   * @return a function that lifts the Try out of the third tuple element
   */
  public static <T, U, V, W> Function1<Tuple4<T, U, Try<V>, W>, Try<Tuple4<T, U, V, W>>> fromTuple4$3() {
    return tuple4 -> tuple4._3().map(v -> Tuple.of(tuple4._1, tuple4._2, v, tuple4._4));
  }

  /**
   * returns a function that transforms a
   * Tuple4{Try{T}, U, Try{V}, W}
   * to
   * Try{Tuple4{T,U,V,W}}
   *
   * @param <T> the success type wrapped in the first Try element
   * @param <U> the type of the plain second element
   * @param <V> the success type wrapped in the third Try element
   * @param <W> the type of the plain fourth element
   * @return a function that lifts the Try values out of the first and third tuple elements
   */
  public static <T, U, V, W> Function1<Tuple4<Try<T>, U, Try<V>, W>, Try<Tuple4<T, U, V, W>>> fromTuple4$1$3() {
    return tuple4 -> LiftTry.<T, U, Try<V>, W>fromTuple4$1()
        .apply(tuple4)
        .flatMap(LiftTry.fromTuple4$3());

  }


  /**
   * returns a function that transforms a HashMap with Try values into a Try of HashMap
   *
   * @param <S> the key type
   * @param <T> the success type of the Try values
   * @return a function that lifts all Try values in the map into a single Try of a HashMap
   */
  public static <S, T> Function1<HashMap<S, Try<T>>, Try<HashMap<S, T>>> fromHashMapValue() {
    return hashMap -> hashMap.foldLeft(
      Try.success(HashMap.empty()),
      (theTry, entry) -> theTry.flatMap(newMap -> entry._2.map(value -> newMap.put(entry._1, value))));
  }

  /**
   * returns a function that transforms a HashMap with Try keys into a Try of HashMap
   *
   * @param <S> the success type of the Try keys
   * @param <T> the value type
   * @return a function that lifts all Try keys in the map into a single Try of a HashMap
   */
  public static <S, T> Function1<HashMap<Try<S>, T>, Try<HashMap<S, T>>> fromHashMapKey() {
    return hashMap -> hashMap.foldLeft(
      Try.success(HashMap.empty()),
      (theTry, entry) -> theTry.flatMap(newMap -> entry._1.map(key -> newMap.put(key, entry._2))));
  }


  /**
   * returns a function that transforms a HashMap with Try keys and Try values into a Try of HashMap
   *
   * @param <S> the success type of the Try keys
   * @param <T> the success type of the Try values
   * @return a function that lifts all Try keys and values into a single Try of a HashMap
   */
  public static <S, T> Function1<HashMap<Try<S>, Try<T>>, Try<HashMap<S, T>>> fromHashMap() {
    return hashMap -> hashMap.foldLeft(
      Try.success(HashMap.empty()),
      (theTry, entry) -> theTry.flatMap(accMap -> entry._1.flatMap(key -> entry._2.map(value -> accMap.put(key, value)))));
  }


  /**
   * returns a function that transforms a Map with Try values into a Try of Map
   *
   * @param <S> the key type
   * @param <T> the success type of the Try values
   * @return a function that lifts all Try values in the map into a single Try of a Map
   */
  public static <S, T> Function1<Map<S, Try<T>>, Try<Map<S, T>>> fromMap() {
    return map -> map.foldLeft(
      // have to remove all entries from the map as I don't know which type of map it is (e.g. HashMap, TreeMap, ...)
      // values mapping has to be done so that the return types match, it has no effect as the map is already empty
      Try.success(map.removeAll(map.keySet()).mapValues(Try::get)),
      (theTry, entry) -> theTry.flatMap(newMap -> entry._2.map(value -> newMap.put(entry._1, value))));
  }

  /**
   * returns a function that transforms a List{Try{T}} into a Try{List{T}}
   *
   * @param <T> the success type of the Try elements
   * @return a function that lifts all Try values in the list into a single Try of a List
   */
  public static <T> Function1<List<Try<T>>, Try<List<T>>> fromList() {
    return list -> list.foldLeft(
      Try.success(List.empty()), LiftTry.fromListMapper());
  }

  /**
   * returns a function that appends a Try{T} element to a Try{List{T}} accumulator
   *
   * @param <T> the success type of the Try elements
   * @return a function that flatMaps the element into the accumulated list
   */
  public static <T> Function2<Try<List<T>>, Try<T>, Try<List<T>>> fromListMapper() {
    return (theTry, tryElement) -> theTry.flatMap(newList -> tryElement.map(newList::append));
  }


  /**
   * returns a function that transforms a Seq{Try{T}} into a Try{Seq{T}}
   *
   * @param <T> the success type of the Try elements
   * @return a function that lifts all Try values in the sequence into a single Try of a Seq
   */
  public static <T> Function1<Seq<Try<T>>, Try<Seq<T>>> fromSeq() {
    return seq -> seq.foldLeft(
      Try.success(List.empty()),
      (theTry, entry) -> theTry.flatMap(newList -> entry.map(newList::append)));
  }
}
