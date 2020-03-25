package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.core.base.activities.Activity;
import io.vavr.control.Either;

public interface PerformsTask {
    <PT,R1> Either<Throwable, R1> attemptsTo(
            Activity<PT, R1> a1);
    <PT,R1, R2> Either<Throwable, R2> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2);
    <PT,R1, R2, R3> Either<Throwable, R3> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3);
    <PT,R1, R2, R3, R4> Either<Throwable, R4> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4);
    <PT,R1, R2, R3, R4, R5> Either<Throwable, R5> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5);
    <PT,R1, R2, R3, R4, R5, R6> Either<Throwable, R6> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5,
            Activity<R5, R6> a6);
    <PT,R1, R2, R3, R4, R5, R6, R7> Either<Throwable, R7> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5,
            Activity<R5, R6> a6,
            Activity<R6, R7> a7);
    <PT,R1, R2, R3, R4, R5, R6, R7, R8> Either<Throwable, R8> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5,
            Activity<R5, R6> a6,
            Activity<R6, R7> a7,
            Activity<R7, R8> a8);
    <PT,R1, R2, R3, R4, R5, R6, R7, R8, R9> Either<Throwable, R9> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5,
            Activity<R5, R6> a6,
            Activity<R6, R7> a7,
            Activity<R7, R8> a8,
            Activity<R8, R9> a9);
    <PT,R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Either<Throwable, R10> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5,
            Activity<R5, R6> a6,
            Activity<R6, R7> a7,
            Activity<R7, R8> a8,
            Activity<R8, R9> a9,
            Activity<R9, R10> a10);
//    <PT,R1> Either<Throwable, R1> attemptsTo(Activity<PT, R1> a1);
//    <PT,R1> Either<Throwable, R1> attemptsTo(Activity<PT, R1> a1);
//    <PT,R1> Either<Throwable, R1> attemptsTo(Activity<PT, R1> a1);
//    <PT,R1> Either<Throwable, R1> attemptsTo(Activity<PT, R1> a1);
//    <PT,R1> Either<Throwable, R1> attemptsTo(Activity<PT, R1> a1);
//    <PT,R1> Either<Throwable, R1> attemptsTo(Activity<PT, R1> a1);
//    <PT,R1> Either<Throwable, R1> attemptsTo(Activity<PT, R1> a1);
//    <PT,R1> Either<Throwable, R1> attemptsTo(Activity<PT, R1> a1);
}
