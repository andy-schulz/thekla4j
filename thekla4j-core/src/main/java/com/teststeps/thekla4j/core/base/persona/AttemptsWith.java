package com.teststeps.thekla4j.core.base.persona;

/**
 * A functional interface for passing a data object to the first activity
 *
 * @param <T> - the type of the data object
 * @param <R> - the type of the result of the last activity
 */
@FunctionalInterface
public interface AttemptsWith<T, R> {

    /**
     * Pass the data object to the first activity
     *
     * @param t - the data object
     * @return - the result of the first activity
     */
  R using(T t);
}
