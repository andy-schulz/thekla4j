package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.Function1;
import io.vavr.control.Either;

public interface PerformsTask {

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1> Function1<P, Either<ActivityError, R1>> attemptsTo_(
      Activity<P, R1> a1);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2> Function1<P, Either<ActivityError, R2>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3> Function1<P, Either<ActivityError, R3>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4> Function1<P, Either<ActivityError, R4>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5> Function1<P, Either<ActivityError, R5>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6> Function1<P, Either<ActivityError, R6>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6, R7> Function1<P, Either<ActivityError, R7>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7);

  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8> Function1<P, Either<ActivityError, R8>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8);

  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> Function1<P, Either<ActivityError, R9>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9);

  // method naming conventions, use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S100", "java:S107", "java:S119"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Function1<P, Either<ActivityError, R10>> attemptsTo_(
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

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1> Either<ActivityError, R1> attemptsTo(
      Activity<P, R1> a1);

  <P, R1, R2> Either<ActivityError, R2> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2);

  <P, R1, R2, R3> Either<ActivityError, R3> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3);

  <P, R1, R2, R3, R4> Either<ActivityError, R4> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4);

  <P, R1, R2, R3, R4, R5> Either<ActivityError, R5> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5);

  <P, R1, R2, R3, R4, R5, R6> Either<ActivityError, R6> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6);

  <P, R1, R2, R3, R4, R5, R6, R7> Either<ActivityError, R7> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7);

  // use more than 7 parameters
  @SuppressWarnings({"java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8> Either<ActivityError, R8> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8);

  // use more than 7 parameters
  @SuppressWarnings({"java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> Either<ActivityError, R9> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9);

  // use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S107", "java:S119"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Either<ActivityError, R10> attemptsTo(
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

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1> Function1<P, Either<ActivityError, R1>> attemptsTo$_(
      Activity<P, R1> a1,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2> Function1<P, Either<ActivityError, R2>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3> Function1<P, Either<ActivityError, R3>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4> Function1<P, Either<ActivityError, R4>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5> Function1<P, Either<ActivityError, R5>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6> Function1<P, Either<ActivityError, R6>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6, R7> Function1<P, Either<ActivityError, R7>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      String group,
      String groupName);

  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8> Function1<P, Either<ActivityError, R8>> attemptsTo$_(
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

  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> Function1<P, Either<ActivityError, R9>> attemptsTo$_(
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

  // method naming conventions, use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S100", "java:S107", "java:S119"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Function1<P, Either<ActivityError, R10>> attemptsTo$_(
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

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1> Either<ActivityError, R1> attemptsTo$(
      Activity<P, R1> a1,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2> Either<ActivityError, R2> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3> Either<ActivityError, R3> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4> Either<ActivityError, R4> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5> Either<ActivityError, R5> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6> Either<ActivityError, R6> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      String group,
      String groupName);

  // method naming conventions
  @SuppressWarnings({"java:S100"})
  <P, R1, R2, R3, R4, R5, R6, R7> Either<ActivityError, R7> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      String group,
      String groupName);

  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8> Either<ActivityError, R8> attemptsTo$(
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

  // method naming conventions, use more than 7 parameters
  @SuppressWarnings({"java:S100", "java:S107"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> Either<ActivityError, R9> attemptsTo$(
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

  // method naming conventions, use more than 7 parameters, generic naming convention
  @SuppressWarnings({"java:S100", "java:S107", "java:S119"})
  <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Either<ActivityError, R10> attemptsTo$(
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
}
