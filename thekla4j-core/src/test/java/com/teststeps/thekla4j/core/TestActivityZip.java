package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.API;
import com.teststeps.thekla4j.core.activities.Zip;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.SupplyNumber;
import com.teststeps.thekla4j.core.tasks.SupplyString;
import io.vavr.Tuple3;
import io.vavr.Tuple8;
import io.vavr.control.Either;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public class TestActivityZip {

  @Test
  void zipThreeTasksReturnsTupleOfResultsInOrder() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Tuple3<Integer, String, Integer>> result = actor.attemptsTo(
      API.zip(
        SupplyNumber.supplyNumber(1),
        SupplyString.shallThrow(false),
        SupplyNumber.supplyNumber(3)));

    assertThat("is right", result.isRight(), equalTo(true));
    assertThat("first result", result.get()._1(), equalTo(1));
    assertThat("second result", result.get()._2(), equalTo("Hello World"));
    assertThat("third result", result.get()._3(), equalTo(3));
  }

  @Test
  void zipUsableAsStandaloneTaskViaZipOf() {
    Actor actor = Actor.named("TestUser");

    Zip<Tuple3<Integer, String, Integer>> task = Zip.of(
      SupplyNumber.supplyNumber(1),
      SupplyString.shallThrow(false),
      SupplyNumber.supplyNumber(3));

    Either<ActivityError, Tuple3<Integer, String, Integer>> result = task.runAs(actor);

    assertThat("is right", result.isRight(), equalTo(true));
    assertThat("second result", result.get()._2(), equalTo("Hello World"));
  }

  @Test
  void zipResultComposesWithMap() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Integer> result = actor.attemptsTo(
      API.zip(
        SupplyNumber.supplyNumber(42),
        SupplyNumber.supplyNumber(99))
          .map(io.vavr.Tuple2::_1));

    assertThat("is right", result.isRight(), equalTo(true));
    assertThat("first component", result.get(), equalTo(42));
  }

  @Test
  void zipShortCircuitsOnFirstFailureAndSkipsLaterTasks() {
    Actor actor = Actor.named("TestUser");
    AtomicInteger trailingInvocations = new AtomicInteger(0);

    SupplierTask<Integer> trailing = new SupplierTask<>() {
      @Override
      protected Either<ActivityError, Integer> performAs(Actor a) {
        trailingInvocations.incrementAndGet();
        return Either.right(0);
      }
    };

    Either<ActivityError, ?> result = actor.attemptsTo(
      API.zip(
        SupplyNumber.supplyNumber(1),
        SupplyString.shallThrow(true),
        trailing));

    assertThat("is left", result.isLeft(), equalTo(true));
    assertThat("trailing task was not invoked", trailingInvocations.get(), equalTo(0));
  }

  @Test
  void zipEightTasksReturnsPopulatedTuple8() {
    Actor actor = Actor.named("TestUser");

    Either<ActivityError, Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> result =
        actor.attemptsTo(
          API.zip(
            SupplyNumber.supplyNumber(1),
            SupplyNumber.supplyNumber(2),
            SupplyNumber.supplyNumber(3),
            SupplyNumber.supplyNumber(4),
            SupplyNumber.supplyNumber(5),
            SupplyNumber.supplyNumber(6),
            SupplyNumber.supplyNumber(7),
            SupplyNumber.supplyNumber(8)));

    assertThat("is right", result.isRight(), equalTo(true));
    assertThat("first result", result.get()._1(), equalTo(1));
    assertThat("eighth result", result.get()._8(), equalTo(8));
  }
}
