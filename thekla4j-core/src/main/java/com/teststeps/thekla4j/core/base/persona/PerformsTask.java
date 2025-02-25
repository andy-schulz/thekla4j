package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.control.Either;

/**
 * Interface for an actor that can perform tasks
 */
public interface PerformsTask {

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1> AttemptsWith<P, Either<ActivityError, R1>> attemptsTo_(
      Activity<P, R1> a1);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2> AttemptsWith<P, Either<ActivityError, R2>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3> AttemptsWith<P, Either<ActivityError, R3>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4> AttemptsWith<P, Either<ActivityError, R4>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4);


  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5> AttemptsWith<P, Either<ActivityError, R5>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6> AttemptsWith<P, Either<ActivityError, R6>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6, R7> AttemptsWith<P, Either<ActivityError, R7>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the last activity
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8> AttemptsWith<P, Either<ActivityError, R8>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @param a9 the ninth activity
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the eighth activity
   * @param <R9> the type of the result of the last activity
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> AttemptsWith<P, Either<ActivityError, R9>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @param a9 the ninth activity
   * @param a10 the tenth activity
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the eighth activity
   * @param <R9> the type of the result of the ninth activity
   * @param <R10> the type of the result of the last activity
   */
  // method naming conventions, use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S100", "java:S107", "java:S119"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> AttemptsWith<P, Either<ActivityError, R10>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      Activity<R9, R10> a10);


  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @return the result of the last activity
   * @param <R1> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1> Either<ActivityError, R1> attemptsTo(
      Activity<Void, R1> a1);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the last activity
   */
  <R1, R2> Either<ActivityError, R2> attemptsTo(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the last activity
   */
  <R1, R2, R3> Either<ActivityError, R3> attemptsTo(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the last activity
   */
  <R1, R2, R3, R4> Either<ActivityError, R4> attemptsTo(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the last activity
   */
  <R1, R2, R3, R4, R5> Either<ActivityError, R5> attemptsTo(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the last activity
   */
  <R1, R2, R3, R4, R5, R6> Either<ActivityError, R6> attemptsTo(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the last activity
   */
  <R1, R2, R3, R4, R5, R6, R7> Either<ActivityError, R7> attemptsTo(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the last activity
   */
  // use more than 7 parameters
  @SuppressWarnings({"java:S107"})
  <R1, R2, R3, R4, R5, R6, R7, R8> Either<ActivityError, R8> attemptsTo(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @param a9 the ninth activity
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the eighth activity
   * @param <R9> the type of the result of the last activity
   */
  // use more than 7 parameters
  @SuppressWarnings({"java:S107"})
  <R1, R2, R3, R4, R5, R6, R7, R8, R9> Either<ActivityError, R9> attemptsTo(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9);


  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @param a9 the ninth activity
   * @param a10 the tenth activity
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the eighth activity
   * @param <R9> the type of the result of the ninth activity
   * @param <R10> the type of the result of the last activity
   */
  // use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S107", "java:S119"})
  <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Either<ActivityError, R10> attemptsTo(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      Activity<R9, R10> a10);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1> AttemptsWith<P, Either<ActivityError, R1>> attemptsTo$_(
      Activity<P, R1> a1,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2> AttemptsWith<P, Either<ActivityError, R2>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3> AttemptsWith<P, Either<ActivityError, R3>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4> AttemptsWith<P, Either<ActivityError, R4>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5> AttemptsWith<P, Either<ActivityError, R5>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6> AttemptsWith<P, Either<ActivityError, R6>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6, R7> AttemptsWith<P, Either<ActivityError, R7>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the last activity
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8> AttemptsWith<P, Either<ActivityError, R8>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @param a9 the ninth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the eighth activity
   * @param <R9> the type of the result of the last activity
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> AttemptsWith<P, Either<ActivityError, R9>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @param a9 the ninth activity
   * @param a10 the tenth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <P> the type of the input to the first activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the eighth activity
   * @param <R9> the type of the result of the ninth activity
   * @param <R10> the type of the result of the last activity
   */
  // method naming conventions, use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S100", "java:S107", "java:S119"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> AttemptsWith<P, Either<ActivityError, R10>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      Activity<R9, R10> a10,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <R1> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1> Either<ActivityError, R1> attemptsTo$(
      Activity<Void, R1> a1,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2> Either<ActivityError, R2> attemptsTo$(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2, R3> Either<ActivityError, R3> attemptsTo$(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2, R3, R4> Either<ActivityError, R4> attemptsTo$(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2, R3, R4, R5> Either<ActivityError, R5> attemptsTo$(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2, R3, R4, R5, R6> Either<ActivityError, R6> attemptsTo$(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the last activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2, R3, R4, R5, R6, R7> Either<ActivityError, R7> attemptsTo$(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the last activity
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <R1, R2, R3, R4, R5, R6, R7, R8> Either<ActivityError, R8> attemptsTo$(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @param a9 the ninth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the eighth activity
   * @param <R9> the type of the result of the last activity
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <R1, R2, R3, R4, R5, R6, R7, R8, R9> Either<ActivityError, R9> attemptsTo$(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      String group,
      String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param a8 the eighth activity
   * @param a9 the ninth activity
   * @param a10 the tenth activity
   * @param group the activity log group name
   * @param groupName the activity log group description
   * @return the result of the last activity
   * @param <R1> the type of the result of the first activity
   * @param <R2> the type of the result of the second activity
   * @param <R3> the type of the result of the third activity
   * @param <R4> the type of the result of the fourth activity
   * @param <R5> the type of the result of the fifth activity
   * @param <R6> the type of the result of the sixth activity
   * @param <R7> the type of the result of the seventh activity
   * @param <R8> the type of the result of the eighth activity
   * @param <R9> the type of the result of the ninth activity
   * @param <R10> the type of the result of the last activity
   */
  // method naming conventions, use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S100", "java:S107", "java:S119"})
  <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Either<ActivityError, R10> attemptsTo$(
      Activity<Void, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      Activity<R9, R10> a10,
      String group,
      String groupName);
}
