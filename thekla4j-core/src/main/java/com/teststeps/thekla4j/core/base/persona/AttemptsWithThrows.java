package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.commons.error.ActivityError;

/**
 * A functional interface for passing a data object to the first activity that throws an error
 * 
 * @param <T> - the type of the data object
 * @param <R> - the type of the result of the last activity
 */
@FunctionalInterface
public interface AttemptsWithThrows<T, R> {

  /**
   * Pass a data object to the first activity that throws an error
   * 
   * @param t - the data object
   * @return - the result of the last activity
   * @throws ActivityError - if an error occurs
   */
  R using(T t) throws ActivityError;
}
