package com.teststeps.thekla4j.utils.vavr;

import io.vavr.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;


/**
 * Utility class providing functions to lift {@link io.vavr.control.Either} values
 * out of tuples, options, maps, and lists.
 */
public class LiftEither {

  /**
   * transforming an Either{L,R} into a Try{R}
   *
   * @param <E>    the error type of the Either
   * @param <U>    the success type of the Either
   * @param either Either{L,R}
   * @return Either{L,R}
   */
  public static <U, E> Try<U> toTry(Either<E, U> either) {
    return either.isLeft() ? Try.failure((Throwable) either.getLeft()) : Try.success(either.get());
  }

  /**
   * transforming an Option{R} into an Either{L,R}
   *
   * @param <L>           the left (error) type
   * @param <R>           the right (success) type
   * @param errorFunction creating the error object of type L
   * @return Either{L,R}
   */
  public static <L, R> Function1<Option<R>, Either<L, R>> fromOption(Function0<L> errorFunction) {
    return option -> option.isEmpty() ? Either.left(errorFunction.apply()) :
        Either.right(option.get());
  }


  /**
   * returns a function that transforms a Tuple1{Either{T}} to Either{Tuple1{T}}
   *
   * @param <E> the error type
   * @param <T> the success type
   * @return an Either lifted from the Tuple
   */
  public static <E extends Throwable, T> Function1<Tuple1<Either<E, T>>, Either<E, Tuple1<T>>> fromTuple1() {
    return tuple1 -> tuple1.apply(toTuple1());
  }

  /**
   * returns a function that wraps an Either{T} value into Either{Tuple1{T}}
   *
   * @param <E> the error type
   * @param <T> the success type
   * @return a function wrapping the value in a Tuple1
   */
  public static <E extends Throwable, T> Function1<Either<E, T>, Either<E, Tuple1<T>>> toTuple1() {
    return u1 -> u1.map(Tuple::of);
  }

  /**
   * returns a function that transforms a Tuple2{Either{T}, U} to Either{Tuple2{T,U}}
   *
   * @param <E> the error type
   * @param <T> the success type of the first element
   * @param <U> the type of the second element
   * @return a function that lifts the Either out of the first tuple element
   */
  public static <E extends Throwable, T, U> Function1<Tuple2<Either<E, T>, U>, Either<E, Tuple2<T, U>>> fromTuple2$1() {
    return tuple2 -> tuple2.apply(toTuple2$1());
  }

  /**
   * returns a function that combines Either{U1} and U2 into Either{Tuple2{U1,U2}}
   *
   * @param <E>  the error type
   * @param <U1> the success type of the first Either
   * @param <U2> the type of the plain second value
   * @return a function that maps the Either value into a Tuple2
   */
  public static <E extends Throwable, U1, U2> Function2<Either<E, U1>, U2, Either<E, Tuple2<U1, U2>>> toTuple2$1() {
    return (t1, u2) -> t1.map(u1 -> Tuple.of(u1, u2));
  }


  /**
   * returns a function that transforms a Tuple2{T, Either{U}} to Either{Tuple2{T,U}}
   *
   * @param <E> the error type
   * @param <T> the type of the first element
   * @param <U> the success type of the second element
   * @return a function that lifts the Either out of the second tuple element
   */
  public static <E extends Throwable, T, U> Function1<Tuple2<T, Either<E, U>>, Either<E, Tuple2<T, U>>> fromTuple2$2() {
    return tuple2 -> tuple2.apply(toTuple2$2());
  }

  /**
   * returns a function that combines U1 and Either{U2} into Either{Tuple2{U1,U2}}
   *
   * @param <E>  the error type
   * @param <U1> the type of the plain first value
   * @param <U2> the success type of the second Either
   * @return a function that maps the Either value into a Tuple2
   */
  public static <E extends Throwable, U1, U2> Function2<U1, Either<E, U2>, Either<E, Tuple2<U1, U2>>> toTuple2$2() {
    return (u1, t2) -> t2.map(u2 -> Tuple.of(u1, u2));
  }

  /**
   * returns a function that transforms a Tuple2{Either{T}, Either{U}} to Either{Tuple2{T,U}}
   *
   * @param <E> the error type
   * @param <T> the success type of the first Either
   * @param <U> the success type of the second Either
   * @return a function that lifts both Either values into a single Either of a Tuple2
   */
  public static <E extends Throwable, T, U> Function1<Tuple2<Either<E, T>, Either<E, U>>, Either<? extends Throwable, Tuple2<T, U>>> fromTuple2() {
    return tuple2 -> tuple2.apply(toTuple2());
  }

  /**
   * returns a function that combines Either{U1} and Either{U2} into Either{Tuple2{U1,U2}}
   *
   * @param <E>  the error type
   * @param <U1> the success type of the first Either
   * @param <U2> the success type of the second Either
   * @return a function that flatMaps both Either values into a single Either of a Tuple2
   */
  public static <E extends Throwable, U1, U2> Function2<Either<E, U1>, Either<E, U2>, Either<E, Tuple2<U1, U2>>> toTuple2() {
    return (t1, t2) -> t1.flatMap(u1 -> t2.map(u2 -> Tuple.of(u1, u2)));
  }

  /**
   * returns a function that transforms a Tuple3{Either{T}, Either{U}, Either{V}} to Either{Tuple3{T,U,V}}
   *
   * @param <E> the error type
   * @param <T> the success type of the first Either
   * @param <U> the success type of the second Either
   * @param <V> the success type of the third Either
   * @return a function that lifts all three Either values into a single Either of a Tuple3
   */
  public static <E extends Throwable, T, U, V> Function1<Tuple3<Either<E, T>, Either<E, U>, Either<E, V>>, Either<E, Tuple3<T, U, V>>> fromTuple3() {
    return tuple3 -> tuple3.apply(toTuple3());
  }

  /**
   * returns a function that combines Either{U1}, Either{U2} and Either{U3} into Either{Tuple3{U1,U2,U3}}
   *
   * @param <E>  the error type
   * @param <U1> the success type of the first Either
   * @param <U2> the success type of the second Either
   * @param <U3> the success type of the third Either
   * @return a function that flatMaps all three Either values into a single Either of a Tuple3
   */
  public static <E extends Throwable, U1, U2, U3> Function3<Either<E, U1>, Either<E, U2>, Either<E, U3>, Either<E, Tuple3<U1, U2, U3>>> toTuple3() {
    return (t1, t2, t3) -> t1.flatMap(u1 -> t2.flatMap(u2 -> t3.map(u3 -> Tuple.of(u1, u2, u3))));
  }

  /**
   * returns a function that transforms a Tuple3{Either{T}, U, V} to Either{Tuple3{T,U,V}}
   *
   * @param <E> the error type
   * @param <T> the success type of the wrapped Either element
   * @param <U> the type of the second element
   * @param <V> the type of the third element
   * @return a function that lifts the Either out of the first tuple element
   */
  public static <E extends Throwable, T, U, V> Function1<Tuple3<Either<E, T>, U, V>, Either<E, Tuple3<T, U, V>>> fromTuple3$1() {
    return tup3 -> tup3.apply(toTuple3$1());
  }

  /**
   * returns a function that combines Either{U1}, U2 and U3 into Either{Tuple3{U1,U2,U3}}
   *
   * @param <E>  the error type
   * @param <U1> the success type of the first Either
   * @param <U2> the type of the plain second value
   * @param <U3> the type of the plain third value
   * @return a function that maps the Either value into a Tuple3
   */
  public static <E extends Throwable, U1, U2, U3> Function3<Either<E, U1>, U2, U3, Either<E, Tuple3<U1, U2, U3>>> toTuple3$1() {
    return (et1, t2, t3) -> et1.map(u1 -> Tuple.of(u1, t2, t3));
  }

  /**
   * returns a function that transforms a Tuple3{T, Either{U}, V} to Either{Tuple3{T,U,V}}
   *
   * @param <E> the error type
   * @param <T> the type of the first element
   * @param <U> the success type of the wrapped Either element
   * @param <V> the type of the third element
   * @return a function that lifts the Either out of the second tuple element
   */
  public static <E extends Throwable, T, U, V> Function1<Tuple3<T, Either<E, U>, V>, Either<E, Tuple3<T, U, V>>> fromTuple3$2() {
    return tup3 -> tup3.apply(toTuple3$2());
  }

  /**
   * returns a function that combines U1, Either{U2} and U3 into Either{Tuple3{U1,U2,U3}}
   *
   * @param <E>  the error type
   * @param <U1> the type of the plain first value
   * @param <U2> the success type of the second Either
   * @param <U3> the type of the plain third value
   * @return a function that maps the Either value into a Tuple3
   */
  public static <E extends Throwable, U1, U2, U3> Function3<U1, Either<E, U2>, U3, Either<E, Tuple3<U1, U2, U3>>> toTuple3$2() {
    return (t1, et2, t3) -> et2.map(u2 -> Tuple.of(t1, u2, t3));
  }

  /**
   * returns a function that transforms a Tuple3{T, U, Either{V}} to Either{Tuple3{T,U,V}}
   *
   * @param <E> the error type
   * @param <T> the type of the first element
   * @param <U> the type of the second element
   * @param <V> the success type of the wrapped Either element
   * @return a function that lifts the Either out of the third tuple element
   */
  public static <E extends Throwable, T, U, V> Function1<Tuple3<T, U, Either<E, V>>, Either<E, Tuple3<T, U, V>>> fromTuple3$3() {
    return tup3 -> tup3.apply(toTuple3$3());
  }

  /**
   * returns a function that combines U1, U2 and Either{U3} into Either{Tuple3{U1,U2,U3}}
   *
   * @param <E>  the error type
   * @param <U1> the type of the plain first value
   * @param <U2> the type of the plain second value
   * @param <U3> the success type of the third Either
   * @return a function that maps the Either value into a Tuple3
   */
  public static <E extends Throwable, U1, U2, U3> Function3<U1, U2, Either<E, U3>, Either<E, Tuple3<U1, U2, U3>>> toTuple3$3() {
    return (t1, t2, et3) -> et3.map(u3 -> Tuple.of(t1, t2, u3));
  }


  /**
   * returns a function that transforms a Tuple4{Either{T}, Either{U}, Either{V}, Either{W}} to Either{Tuple4{T,U,V,W}}
   *
   * @param <E> the error type
   * @param <T> the success type of the first Either
   * @param <U> the success type of the second Either
   * @param <V> the success type of the third Either
   * @param <W> the success type of the fourth Either
   * @return a function that lifts all four Either values into a single Either of a Tuple4
   */
  public static <E extends Throwable, T, U, V, W> Function1<Tuple4<Either<E, T>, Either<E, U>, Either<E, V>, Either<E, W>>, Either<E, Tuple4<T, U, V, W>>> fromTuple4() {
    return tuple4 -> tuple4.apply(toTuple4());
  }

  /**
   * returns a function that combines four Either values into Either{Tuple4{U1,U2,U3,U4}}
   *
   * @param <E>  the error type
   * @param <U1> the success type of the first Either
   * @param <U2> the success type of the second Either
   * @param <U3> the success type of the third Either
   * @param <U4> the success type of the fourth Either
   * @return a function that flatMaps all four Either values into a single Either of a Tuple4
   */
  public static <E extends Throwable, U1, U2, U3, U4> Function4<Either<E, U1>, Either<E, U2>, Either<E, U3>, Either<E, U4>, Either<E, Tuple4<U1, U2, U3, U4>>> toTuple4() {
    return (t1, t2, t3, t4) -> t1.flatMap(u1 -> t2.flatMap(u2 -> t3.flatMap(u3 -> t4.map(u4 -> Tuple.of(u1, u2, u3, u4)))));
  }

  /**
   * returns a function that transforms a Tuple5{Either{T}, Either{U}, Either{V}, Either{W}, Either{X}} to
   * Either{Tuple5{T,U,V,W,X}}
   *
   * @param <E> the error type
   * @param <T> the success type of the first Either
   * @param <U> the success type of the second Either
   * @param <V> the success type of the third Either
   * @param <W> the success type of the fourth Either
   * @param <X> the success type of the fifth Either
   * @return a function that lifts all five Either values into a single Either of a Tuple5
   */
  public static <E extends Throwable, T, U, V, W, X> Function1<Tuple5<Either<E, T>, Either<E, U>, Either<E, V>, Either<E, W>, Either<E, X>>, Either<E, Tuple5<T, U, V, W, X>>> fromTuple5() {
    return tuple5 -> tuple5.apply(toTuple5());
  }

  /**
   * returns a function that combines five Either values into Either{Tuple5{U1,U2,U3,U4,U5}}
   *
   * @param <E>  the error type
   * @param <U1> the success type of the first Either
   * @param <U2> the success type of the second Either
   * @param <U3> the success type of the third Either
   * @param <U4> the success type of the fourth Either
   * @param <U5> the success type of the fifth Either
   * @return a function that flatMaps all five Either values into a single Either of a Tuple5
   */
  public static <E extends Throwable, U1, U2, U3, U4, U5> Function5<Either<E, U1>, Either<E, U2>, Either<E, U3>, Either<E, U4>, Either<E, U5>, Either<E, Tuple5<U1, U2, U3, U4, U5>>> toTuple5() {
    return (t1, t2, t3, t4, t5) -> t1.flatMap(u1 -> t2.flatMap(u2 -> t3.flatMap(u3 -> t4.flatMap(u4 -> t5.map(u5 -> Tuple.of(u1, u2, u3, u4, u5))))));
  }

  /**
   * returns a function that transforms a HashMap{T, Either{Throwable, U}} to Either{Throwable, HashMap{T, U}}
   *
   * @param <E> the error type
   * @param <T> the key type of the HashMap
   * @param <U> the success type of the Either values
   * @return a function that lifts all Either values in the map into a single Either of a HashMap
   */
  public static <E extends Throwable, T, U> Function1<HashMap<T, Either<E, U>>, Either<E, HashMap<T, U>>> fromHashMap() {
    return hashMapE -> hashMapE.foldLeft(
      Either.<E, HashMap<T, U>>right(HashMap.empty()),
      (theEither, entry) -> theEither.flatMap(newMap -> entry._2.map(value -> newMap.put(entry._1, value))));
  }

  /**
   * returns a function that transforms a List{Either{E, U}} to Either{E, List{U}}
   *
   * @param <E> the error type
   * @param <U> the success type of the Either elements
   * @return a function that lifts all Either values in the list into a single Either of a List
   */
  public static <E extends Throwable, U> Function1<List<Either<E, U>>, Either<E, List<U>>> fromList() {
    return list -> list.foldLeft(
      Either.<E, List<U>>right(List.empty()),
      (theEither, entry) -> theEither.flatMap(newList -> entry.map(newList::append)));
  }

  /**
   * returns a function that accumulates an Either{E, U} into an Either{E, List{U}}
   *
   * @param <E> the error type
   * @param <U> the success type of the Either elements
   * @return a function that appends an Either value to an Either-wrapped List
   */
  public static <E extends Throwable, U> Function2<Either<E, List<U>>, Either<E, U>, Either<E, List<U>>> toList() {
    return (acc, element) -> acc.flatMap(list -> element.map(list::append));
  }
}