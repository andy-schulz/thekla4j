package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.commons.error.ActivityError;

/**
 * A performer is an actor that can perform activities
 * It is a decorator for an actor that can perform activities and is throwing ActivityError in case of failure
 */
public interface Performer {

  /**
   * decorate the given actor to perform activities but is throwing an ActivityError in case of failure
   * @param actor the actor to perform the activities
   * @return the performer
   */
  static Performer of(Actor actor) {
    return new PerformerDecorator(actor);
  }

  /**
  * get the decorated actor
  * @return the actor
  */
  Actor actor();

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1> AttemptsWithThrows<P, R1> attemptsTo_(
    Activity<P, R1> a1) throws ActivityError;


  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @throws ActivityError if any of the activities fails
   */
  <P, R1, R2> AttemptsWithThrows<P, R2> attemptsTo_(
    Activity<P, R1> a1,
    Activity<R1, R2> a2) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3> AttemptsWithThrows<P, R3> attemptsTo_(
    Activity<P, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4> AttemptsWithThrows<P, R4> attemptsTo_(
    Activity<P, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5> AttemptsWithThrows<P, R5> attemptsTo_(
    Activity<P, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6> AttemptsWithThrows<P, R6> attemptsTo_(
    Activity<P, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6) throws ActivityError;

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
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6, R7> AttemptsWithThrows<P, R7> attemptsTo_(
    Activity<P, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6,
    Activity<R6, R7> a7) throws ActivityError;

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
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8> AttemptsWithThrows<P, R8> attemptsTo_(
    Activity<P, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6,
    Activity<R6, R7> a7,
    Activity<R7, R8> a8) throws ActivityError;

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
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   * @param <R9> the output type of the ninth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> AttemptsWithThrows<P, R9> attemptsTo_(
    Activity<P, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6,
    Activity<R6, R7> a7,
    Activity<R7, R8> a8,
    Activity<R8, R9> a9) throws ActivityError;

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
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   * @param <R9> the output type of the ninth activity
   * @param <R10> the output type of the tenth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions, use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S100", "java:S107", "java:S119"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> AttemptsWithThrows<P, R10> attemptsTo_(
    Activity<P, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6,
    Activity<R6, R7> a7,
    Activity<R7, R8> a8,
    Activity<R8, R9> a9,
    Activity<R9, R10> a10) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1> R1 attemptsTo(
    Activity<Void, R1> a1) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @throws ActivityError if any of the activities fails
   */
  <R1, R2> R2 attemptsTo(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @throws ActivityError if any of the activities fails
   */
  <R1, R2, R3> R3 attemptsTo(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @throws ActivityError if any of the activities fails
   */
  <R1, R2, R3, R4> R4 attemptsTo(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @throws ActivityError if any of the activities fails
   */
  <R1, R2, R3, R4, R5> R5 attemptsTo(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @throws ActivityError if any of the activities fails
   */
  <R1, R2, R3, R4, R5, R6> R6 attemptsTo(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6) throws ActivityError;

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
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @throws ActivityError if any of the activities fails
   */
  <R1, R2, R3, R4, R5, R6, R7> R7 attemptsTo(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6,
    Activity<R6, R7> a7) throws ActivityError;

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
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   * @throws ActivityError if any of the activities fails
   */
  // use more than 7 parameters
  @SuppressWarnings({"java:S107"})
  <R1, R2, R3, R4, R5, R6, R7, R8> R8 attemptsTo(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6,
    Activity<R6, R7> a7,
    Activity<R7, R8> a8) throws ActivityError;

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
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   * @param <R9> the output type of the ninth activity
   * @throws ActivityError if any of the activities fails
   */
  // use more than 7 parameters
  @SuppressWarnings({"java:S107"})
  <R1, R2, R3, R4, R5, R6, R7, R8, R9> R9 attemptsTo(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6,
    Activity<R6, R7> a7,
    Activity<R7, R8> a8,
    Activity<R8, R9> a9) throws ActivityError;

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
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   * @param <R9> the output type of the ninth activity
   * @param <R10> the output type of the tenth activity
   * @throws ActivityError if any of the activities fails
   */
  // use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S107", "java:S119"})
  <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> R10 attemptsTo(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6,
    Activity<R6, R7> a7,
    Activity<R7, R8> a8,
    Activity<R8, R9> a9,
    Activity<R9, R10> a10) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1> AttemptsWithThrows<P, R1> attemptsTo$_(
    Activity<P, R1> a1,
    String group,
    String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2> AttemptsWithThrows<P, R2> attemptsTo$_(
    Activity<P, R1> a1,
    Activity<R1, R2> a2,
    String group,
    String groupName);

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3> AttemptsWithThrows<P, R3> attemptsTo$_(
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
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4> AttemptsWithThrows<P, R4> attemptsTo$_(
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
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5> AttemptsWithThrows<P, R5> attemptsTo$_(
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
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6> AttemptsWithThrows<P, R6> attemptsTo$_(
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
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6, R7> AttemptsWithThrows<P, R7> attemptsTo$_(
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
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8> AttemptsWithThrows<P, R8> attemptsTo$_(
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
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   * @param <R9> the output type of the ninth activity
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> AttemptsWithThrows<P, R9> attemptsTo$_(
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
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <P> the input type of the first activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   * @param <R9> the output type of the ninth activity
   * @param <R10> the output type of the tenth activity
   */
  // method naming conventions, use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S100", "java:S107", "java:S119"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> AttemptsWithThrows<P, R10> attemptsTo$_(
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
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1> R1 attemptsTo$(
    Activity<Void, R1> a1,
    String group,
    String groupName) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2> R2 attemptsTo$(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    String group,
    String groupName) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2, R3> R3 attemptsTo$(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    String group,
    String groupName) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2, R3, R4> R4 attemptsTo$(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    String group,
    String groupName) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2, R3, R4, R5> R5 attemptsTo$(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    String group,
    String groupName) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2, R3, R4, R5, R6> R6 attemptsTo$(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6,
    String group,
    String groupName) throws ActivityError;

  /**
   * execute the given activities in sequence
   * @param a1 the first activity
   * @param a2 the second activity
   * @param a3 the third activity
   * @param a4 the fourth activity
   * @param a5 the fifth activity
   * @param a6 the sixth activity
   * @param a7 the seventh activity
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <R1, R2, R3, R4, R5, R6, R7> R7 attemptsTo$(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6,
    Activity<R6, R7> a7,
    String group,
    String groupName) throws ActivityError;

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
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <R1, R2, R3, R4, R5, R6, R7, R8> R8 attemptsTo$(
    Activity<Void, R1> a1,
    Activity<R1, R2> a2,
    Activity<R2, R3> a3,
    Activity<R3, R4> a4,
    Activity<R4, R5> a5,
    Activity<R5, R6> a6,
    Activity<R6, R7> a7,
    Activity<R7, R8> a8,
    String group,
    String groupName) throws ActivityError;

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
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   * @param <R9> the output type of the ninth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <R1, R2, R3, R4, R5, R6, R7, R8, R9> R9 attemptsTo$(
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
    String groupName) throws ActivityError;

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
   * @param group the group name
   * @param groupName the group description
   * @return the result of the last activity
   * @param <R1> the output type of the first activity
   * @param <R2> the output type of the second activity
   * @param <R3> the output type of the third activity
   * @param <R4> the output type of the fourth activity
   * @param <R5> the output type of the fifth activity
   * @param <R6> the output type of the sixth activity
   * @param <R7> the output type of the seventh activity
   * @param <R8> the output type of the eighth activity
   * @param <R9> the output type of the ninth activity
   * @param <R10> the output type of the tenth activity
   * @throws ActivityError if any of the activities fails
   */
  // method naming conventions, use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S100", "java:S107", "java:S119"})
  <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> R10 attemptsTo$(
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
    String groupName) throws ActivityError;
}
