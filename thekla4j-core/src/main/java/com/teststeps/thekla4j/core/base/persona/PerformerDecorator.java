package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.Function1;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class PerformerDecorator implements Performer {
  private Actor actor;

  /**
   * {@inheritDoc}
   */
  @Override
  public Actor actor() {
    return actor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1> AttemptsWithThrows<P, R1> attemptsTo_(Activity<P, R1> a1) {
    return x -> actor.attemptsTo_(a1).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2> AttemptsWithThrows<P, R2> attemptsTo_(Activity<P, R1> a1, Activity<R1, R2> a2) {
    return x -> actor.attemptsTo_(a1, a2).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3> AttemptsWithThrows<P, R3> attemptsTo_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3) {
    return x -> actor.attemptsTo_(a1, a2, a3).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4> AttemptsWithThrows<P, R4> attemptsTo_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4) {
    return x -> actor.attemptsTo_(a1, a2, a3, a4).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5> AttemptsWithThrows<P, R5> attemptsTo_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5) {
    return x -> actor.attemptsTo_(a1, a2, a3, a4, a5).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6> AttemptsWithThrows<P, R6> attemptsTo_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6) {
    return x -> actor.attemptsTo_(a1, a2, a3, a4, a5, a6).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7> AttemptsWithThrows<P, R7> attemptsTo_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7) {
    return x -> actor.attemptsTo_(a1, a2, a3, a4, a5, a6, a7).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8> AttemptsWithThrows<P, R8> attemptsTo_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8) {
    return x -> actor.attemptsTo_(a1, a2, a3, a4, a5, a6, a7, a8).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> AttemptsWithThrows<P, R9> attemptsTo_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9) {
    return x -> actor.attemptsTo_(a1, a2, a3, a4, a5, a6, a7, a8, a9).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> AttemptsWithThrows<P, R10> attemptsTo_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, Activity<R9, R10> a10) {
    return x -> actor.attemptsTo_(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1> R1 attemptsTo(Activity<Void, R1> a1) throws ActivityError {
    return actor.attemptsTo(a1).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2> R2 attemptsTo(Activity<Void, R1> a1, Activity<R1, R2> a2) throws ActivityError {
    return actor.attemptsTo(a1, a2).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3> R3 attemptsTo(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3) throws ActivityError {
    return actor.attemptsTo(a1, a2, a3).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4> R4 attemptsTo(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4) throws ActivityError {
    return actor.attemptsTo(a1, a2, a3, a4).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5> R5 attemptsTo(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5) throws ActivityError {
    return actor.attemptsTo(a1, a2, a3, a4, a5).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6> R6 attemptsTo(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6) throws ActivityError {
    return actor.attemptsTo(a1, a2, a3, a4, a5, a6).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7> R7 attemptsTo(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7) throws ActivityError {
    return actor.attemptsTo(a1, a2, a3, a4, a5, a6, a7).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7, R8> R8 attemptsTo(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8) throws ActivityError {
    return actor.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7, R8, R9> R9 attemptsTo(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9) throws ActivityError {
    return actor.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8, a9).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> R10 attemptsTo(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, Activity<R9, R10> a10) throws ActivityError {
    return actor.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1> AttemptsWithThrows<P, R1> attemptsTo$_(Activity<P, R1> a1, String group, String groupName) {
    return x -> actor.attemptsTo$_(a1, group, groupName).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2> AttemptsWithThrows<P, R2> attemptsTo$_(Activity<P, R1> a1, Activity<R1, R2> a2, String group, String groupName) {
    return x -> actor.attemptsTo$_(a1, a2, group, groupName).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3> AttemptsWithThrows<P, R3> attemptsTo$_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, String group, String groupName) {
    return x -> actor.attemptsTo$_(a1, a2, a3, group, groupName).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4> AttemptsWithThrows<P, R4> attemptsTo$_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, String group, String groupName) {
    return x -> actor.attemptsTo$_(a1, a2, a3, a4, group, groupName).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5> AttemptsWithThrows<P, R5> attemptsTo$_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, String group, String groupName) {
    return x -> actor.attemptsTo$_(a1, a2, a3, a4, a5, group, groupName).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6> AttemptsWithThrows<P, R6> attemptsTo$_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, String group, String groupName) {
    return x -> actor.attemptsTo$_(a1, a2, a3, a4, a5, a6, group, groupName).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7> AttemptsWithThrows<P, R7> attemptsTo$_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, String group, String groupName) {
    return x -> actor.attemptsTo$_(a1, a2, a3, a4, a5, a6, a7, group, groupName).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8> AttemptsWithThrows<P, R8> attemptsTo$_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, String group, String groupName) {
    return x -> actor.attemptsTo$_(a1, a2, a3, a4, a5, a6, a7, a8, group, groupName).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> AttemptsWithThrows<P, R9> attemptsTo$_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, String group, String groupName) {
    return x -> actor.attemptsTo$_(a1, a2, a3, a4, a5, a6, a7, a8, a9, group, groupName).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> AttemptsWithThrows<P, R10> attemptsTo$_(Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, Activity<R9, R10> a10, String group, String groupName) {
    return x -> actor.attemptsTo$_(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, group, groupName).using(x).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1> R1 attemptsTo$(Activity<Void, R1> a1, String group, String groupName) throws ActivityError {
    return actor.attemptsTo$(a1, group, groupName).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2> R2 attemptsTo$(Activity<Void, R1> a1, Activity<R1, R2> a2, String group, String groupName) throws ActivityError {
    return actor.attemptsTo$(a1, a2, group, groupName).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3> R3 attemptsTo$(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, String group, String groupName) throws ActivityError {
    return actor.attemptsTo$(a1, a2, a3, group, groupName).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4> R4 attemptsTo$(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, String group, String groupName) throws ActivityError {
    return actor.attemptsTo$(a1, a2, a3, a4, group, groupName).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5> R5 attemptsTo$(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, String group, String groupName) throws ActivityError {
    return actor.attemptsTo$(a1, a2, a3, a4, a5, group, groupName).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6> R6 attemptsTo$(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, String group, String groupName) throws ActivityError {
    return actor.attemptsTo$(a1, a2, a3, a4, a5, a6, group, groupName).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7> R7 attemptsTo$(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, String group, String groupName) throws ActivityError {
    return actor.attemptsTo$(a1, a2, a3, a4, a5, a6, a7, group, groupName).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7, R8> R8 attemptsTo$(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, String group, String groupName) throws ActivityError {
    return actor.attemptsTo$(a1, a2, a3, a4, a5, a6, a7, a8, group, groupName).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7, R8, R9> R9 attemptsTo$(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, String group, String groupName) throws ActivityError {
    return actor.attemptsTo$(a1, a2, a3, a4, a5, a6, a7, a8, a9, group, groupName).getOrElseThrow(Function1.identity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> R10 attemptsTo$(Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, Activity<R9, R10> a10, String group, String groupName) throws ActivityError {
    return actor.attemptsTo$(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, group, groupName).getOrElseThrow(Function1.identity());
  }

}
