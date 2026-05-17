package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.StaticStringTask;
import com.teststeps.thekla4j.core.tasks.SupplyNumber;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

public class TestActivityDrop {

  @Test
  void taskDropDiscardResult() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo(
      StaticStringTask.with("TestData").drop());

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("result value is null", result.get(), nullValue());
  }

  @Test
  void taskDropCanBeChainedWithSupplierTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Integer> result = actor.attemptsTo(
      StaticStringTask.with("TestData").drop(),
      SupplyNumber.supplyNumber(99));

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("supplier task result is returned", result.get(), equalTo(99));
  }

  @Test
  void supplierTaskDropDiscardResult() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo(
      SupplyNumber.supplyNumber(42).drop());

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("result value is null", result.get(), nullValue());
  }

  @Test
  void interactionMapTransformsResult() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Integer> result = actor.attemptsTo_(
      PassThroughInteraction.start().map(n -> n * 2))
        .using(5);

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("interaction result is mapped", result.get(), equalTo(10));
  }

  @Test
  void interactionDropDiscardResult() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo_(
      PassThroughInteraction.start().drop())
        .using(5);

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("result value is null", result.get(), nullValue());
  }

  @Test
  void seeResultDropPassingAssertion() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo(
      StaticStringTask.with("TestData")
          .is(Expected.to.pass(s -> s.equals("TestData"), "string matches"))
          .drop());

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("result value is null", result.get(), nullValue());
  }

  @Test
  void seeResultDropFailingAssertionPropagatesError() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo(
      StaticStringTask.with("TestData")
          .is(Expected.to.pass(s -> s.equals("WRONG"), "string matches wrong value"))
          .drop());

    assertThat("result is left on assertion failure", result.isLeft(), equalTo(true));
  }

  @Test
  void seeResultDropCanBeChainedWithSupplierTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Integer> result = actor.attemptsTo(
      StaticStringTask.with("TestData")
          .is(Expected.to.pass(s -> s.equals("TestData"), "string matches"))
          .drop(),
      SupplyNumber.supplyNumber(42));

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("supplier task result is returned", result.get(), equalTo(42));
  }

  @Test
  void seeResultMapThenDropDiscardsMappedResult() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Void> result = actor.attemptsTo(
      StaticStringTask.with("TestData")
          .is(Expected.to.pass(s -> s.equals("TestData"), "string matches"))
          .map(String::length)
          .drop());

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("result value is null", result.get(), nullValue());
  }

  @Test
  void seeResultMapThenDropCanBeChainedWithSupplierTask() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Integer> result = actor.attemptsTo(
      StaticStringTask.with("TestData")
          .is(Expected.to.pass(s -> s.equals("TestData"), "string matches"))
          .map(String::length)
          .drop(),
      SupplyNumber.supplyNumber(42));

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("supplier task result is returned", result.get(), equalTo(42));
  }

  @Action("Pass Through Interaction")
  static class PassThroughInteraction extends Interaction<Integer, Integer> {

    @Override
    protected Either<ActivityError, Integer> performAs(Actor actor, Integer input) {
      return Either.right(input);
    }

    public static PassThroughInteraction start() {
      return new PassThroughInteraction();
    }
  }
}
