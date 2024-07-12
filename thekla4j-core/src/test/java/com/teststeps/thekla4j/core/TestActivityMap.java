package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.API;
import com.teststeps.thekla4j.core.activities.Map;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import static com.teststeps.thekla4j.core.activities.API.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestActivityMap {


  @Test
  void testMapFunction() {
    String input = "test";
    String expectedOutput = "TEST";

    Map<String, String> mapTask = map(String::toUpperCase);

    Either<ActivityError, String> result = mapTask.perform(Actor.named("TestUser"), input);

    assertThat("task execution is successful", result.isRight(), equalTo(true));
    assertThat("output is as expected", result.get(), equalTo(expectedOutput));
  }

  @Test
  void testMapFunctionWithReason() {
    String input = "test";
    String expectedOutput = "TEST";
    String reason = "convert input to uppercase";

    Actor actor = Actor.named("TestUser");

    Either<ActivityError, String> result =
      actor.attemptsTo_(
        API.<String, String>map(String::toUpperCase, reason))
        .apply(input);

    assertThat("task execution is successful", result.isRight(), equalTo(true));
    assertThat("output is as expected", result.get(), equalTo(expectedOutput));
    assertThat("activity log contains reason", actor.activityLog.getLogTree().activityNodes.get(0).description, equalTo(reason));
  }

  @Test
  void testMapFunctionWithTry() {
    String input = "test";
    String expectedOutput = "TEST";

    Function1<String, Try<String>> mapper = s -> Try.of(s::toUpperCase);

    Map<String, String> mapTask = API.mapTry(mapper);

    Either<ActivityError, String> result = mapTask.perform(Actor.named("TestUser"), input);

    assertThat("task execution is successful", result.isRight(), equalTo(true));
    assertThat("output is as expected", result.get(), equalTo(expectedOutput));
  }

  @Test
  void testFailingMapFunctionWithTry() {
    String input = "test";

    Function1<String, Try<String>> mapper = s -> Try.of(() -> {
      throw new RuntimeException("Test exception");
    });

    Map<String, String> mapTask = API.mapTry(mapper);

    Either<ActivityError, String> result = mapTask.perform(Actor.named("TestUser"), input);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));
    assertThat("error message is as expected", result.getLeft().getMessage(), equalTo("Test exception"));
  }

  @Test
  void testMapFunctionWithTryAndReason() {
    String input = "test";
    String expectedOutput = "TEST";
    String reason = "convert input to uppercase with Try";

    Actor actor = Actor.named("TestUser");

    Function1<String, Try<String>> mapper = s -> Try.of(s::toUpperCase);

    Either<ActivityError, String> result =
      actor.attemptsTo_(
        API.mapTry(mapper, reason))
        .apply(input);

    assertThat("task execution is successful", result.isRight(), equalTo(true));
    assertThat("output is as expected", result.get(), equalTo(expectedOutput));
    assertThat("activity log contains reason", actor.activityLog.getLogTree().activityNodes.get(0).description, equalTo(reason));
  }

  @Test
  void testFailingMapFunctionWithTryAndReason() {
    String input = "test";
    String reason = "convert input to uppercase with Try";

    Actor actor = Actor.named("TestUser");

    Function1<String, Try<String>> mapper = s -> Try.of(() -> {
      throw new RuntimeException("Test exception");
    });

    Either<ActivityError, String> result =
      actor.attemptsTo_(
        API.mapTry(mapper, reason))
        .apply(input);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));
    assertThat("error message is as expected", result.getLeft().getMessage(), equalTo("Test exception"));
    assertThat("activity log contains reason", actor.activityLog.getLogTree().activityNodes.get(0).description, equalTo(reason));
  }
}
