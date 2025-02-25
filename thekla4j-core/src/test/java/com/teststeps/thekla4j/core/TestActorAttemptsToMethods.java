package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.errors.DetectedNullObject;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.AddNumber;
import com.teststeps.thekla4j.core.tasks.AddNumberToInitialNumberOne;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestActorAttemptsToMethods {

  @Test
  @DisplayName("calling attemptsTo with null task should throw DetectedNullObject exception")
  public void testNullTaskForAttemptsTo() {

    Actor actor = Actor.named("actor");

    Throwable thrown = assertThrows(
      DetectedNullObject.class,
      () -> actor.attemptsTo(null));

    assertThat(thrown.getMessage(), startsWith("Property  ProcessLogAnnotation::activity is null, but should not."));
  }

  @Test
  @DisplayName("calling attemptsTo with null task should throw DetectedNullObject exception")
  public void testNullTaskForAttemptsTo_() {

    Actor actor = Actor.named("actor");

    Throwable thrown = assertThrows(
      DetectedNullObject.class,
      () -> actor.attemptsTo_(null).using(1));

    assertThat(thrown.getMessage(), startsWith("Property  ProcessLogAnnotation::activity is null, but should not."));
  }

  @Test
  @DisplayName("calling attemptsTo with null task should throw DetectedNullObject exception")
  public void testNullTaskForAttemptsTo$() {

    Actor actor = Actor.named("actor");

    Throwable thrown = assertThrows(
      DetectedNullObject.class,
      () -> actor.attemptsTo$(null, "Group", "Group description"));

    assertThat(thrown.getMessage(), startsWith("Property  ProcessLogAnnotation::activity is null, but should not."));
  }

  @Test
  @DisplayName("calling attemptsTo with null task should throw DetectedNullObject exception")
  public void testNullTaskForAttemptsTo$_() {

    Actor actor = Actor.named("actor");

    Throwable thrown = assertThrows(
      DetectedNullObject.class,
      () -> actor.attemptsTo$_(null, "Group", "Group description").using(1));

    assertThat(thrown.getMessage(), startsWith("Property  ProcessLogAnnotation::activity is null, but should not."));
  }

  @Test
  public void testAttemptsToWithOneParameter() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo_(
        AddNumber.of(1))
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(2));
  }

  @Test
  public void testAttemptsToWithTwoParameters() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2))
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(4));
  }

  @Test
  public void testAttemptsToWithThreeParameters() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3))
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(7));
  }

  @Test
  public void testAttemptsToWithFourParameters() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4))
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(11));
  }

  @Test
  public void testAttemptsToWithFiveParameters() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5))
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(16));
  }

  @Test
  public void testAttemptsToWithSixParameters() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6))
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(22));
  }

  @Test
  public void testAttemptsToWithSevenParameters() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7))
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(29));
  }

  @Test
  public void testAttemptsToWithEightParameters() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8))
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(37));
  }

  @Test
  public void testAttemptsToWithNineParameters() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9))
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(46));
  }

  @Test
  public void testAttemptsToWithTenParameters() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        AddNumber.of(10))
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(56));
  }

  @Test
  public void testAttemptsToWithOneParameterWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo(
        AddNumberToInitialNumberOne.of(1));

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(2));
  }

  @Test
  public void testAttemptsToWithTwoParametersWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2));

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(4));
  }

  @Test
  public void testAttemptsToWithThreeParametersWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        AddNumber.of(3));

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(7));
  }

  @Test
  public void testAttemptsToWithFourParametersWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4));

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(11));
  }

  @Test
  public void testAttemptsToWithFiveParametersWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5));

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(16));
  }

  @Test
  public void testAttemptsToWithSixParametersWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6));

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(22));
  }

  @Test
  public void testAttemptsToWithSevenParametersWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7));

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(29));
  }

  @Test
  public void testAttemptsToWithEightParametersWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8));

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(37));
  }

  @Test
  public void testAttemptsToWithNineParametersWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9));

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(46));
  }

  @Test
  public void testAttemptsToWithTenParametersWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        AddNumber.of(10));

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(56));
  }

  @Test
  public void testAttemptsToWithOneParameterWithLogGrouping() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$_(
        AddNumber.of(1),
      "Group", "group description")
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(2));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 1"));
    assertThat("task has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithTwoParametersWithLogGrouping() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
      "Group", "group description")
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(4));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 1"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 2 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithThreeParametersWithLogGrouping() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
      "Group", "group description")
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(7));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 1"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 2 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 3 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithFourParametersWithLogGrouping() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
      "Group", "group description")
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(11));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 1"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 2 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 3 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 4 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

  }

  @Test
  public void testAttemptsToWithFiveParametersWithLogGrouping() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        "Group", "group description")
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(16));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 1"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 2 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 3 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 4 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));

  }

  @Test
  public void testAttemptsToWithSixParametersWithLogGrouping() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        "Group", "group description")
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(22));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 1"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 2 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 3 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 4 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));


    assertThat("group contains task 6",
      log.activityNodes.get(0).activityNodes.get(5).description, equalTo("add number 6 to given number 16"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(5).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithSevenParametersWithLogGrouping() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        "Group", "group description")
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(29));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 1"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 2 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 3 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 4 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));


    assertThat("group contains task 6",
      log.activityNodes.get(0).activityNodes.get(5).description, equalTo("add number 6 to given number 16"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(5).name, equalTo("AddNumber"));

    assertThat("group contains task 7",
      log.activityNodes.get(0).activityNodes.get(6).description, equalTo("add number 7 to given number 22"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(6).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithEightParametersWithLogGrouping() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        "Group", "group description")
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(37));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 1"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 2 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 3 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 4 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));


    assertThat("group contains task 6",
      log.activityNodes.get(0).activityNodes.get(5).description, equalTo("add number 6 to given number 16"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(5).name, equalTo("AddNumber"));

    assertThat("group contains task 7",
      log.activityNodes.get(0).activityNodes.get(6).description, equalTo("add number 7 to given number 22"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(6).name, equalTo("AddNumber"));

    assertThat("group contains task 8",
      log.activityNodes.get(0).activityNodes.get(7).description, equalTo("add number 8 to given number 29"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(7).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithNineParametersWithLogGrouping() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        "Group", "group description")
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(46));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 1"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 2 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 3 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 4 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));


    assertThat("group contains task 6",
      log.activityNodes.get(0).activityNodes.get(5).description, equalTo("add number 6 to given number 16"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(5).name, equalTo("AddNumber"));

    assertThat("group contains task 7",
      log.activityNodes.get(0).activityNodes.get(6).description, equalTo("add number 7 to given number 22"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(6).name, equalTo("AddNumber"));

    assertThat("group contains task 8",
      log.activityNodes.get(0).activityNodes.get(7).description, equalTo("add number 8 to given number 29"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(7).name, equalTo("AddNumber"));

    assertThat("group contains task 9",
      log.activityNodes.get(0).activityNodes.get(8).description, equalTo("add number 9 to given number 37"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(8).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithTenParametersWithLogGrouping() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        AddNumber.of(10),
        "Group", "group description")
      .using(1);

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(56));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 1"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 2 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 3 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 4 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 5 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));

    assertThat("group contains task 6",
      log.activityNodes.get(0).activityNodes.get(5).description, equalTo("add number 6 to given number 16"));
    assertThat("task 6 has correct name", log.activityNodes.get(0).activityNodes.get(5).name, equalTo("AddNumber"));

    assertThat("group contains task 7",
      log.activityNodes.get(0).activityNodes.get(6).description, equalTo("add number 7 to given number 22"));
    assertThat("task 7 has correct name", log.activityNodes.get(0).activityNodes.get(6).name, equalTo("AddNumber"));

    assertThat("group contains task 8",
      log.activityNodes.get(0).activityNodes.get(7).description, equalTo("add number 8 to given number 29"));
    assertThat("task 8 has correct name", log.activityNodes.get(0).activityNodes.get(7).name, equalTo("AddNumber"));

    assertThat("group contains task 9",
      log.activityNodes.get(0).activityNodes.get(8).description, equalTo("add number 9 to given number 37"));
    assertThat("task 9 has correct name", log.activityNodes.get(0).activityNodes.get(8).name, equalTo("AddNumber"));

    assertThat("group contains task 10",
      log.activityNodes.get(0).activityNodes.get(9).description, equalTo("add number 10 to given number 46"));
    assertThat("task 10 has correct name", log.activityNodes.get(0).activityNodes.get(9).name, equalTo("AddNumber"));
  }


  @Test
  public void testAttemptsToWithOneParametersWithLogGroupingWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$(
        AddNumberToInitialNumberOne.of(1),
        "Group", "group description");

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(2));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number to initial number"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumberToInitialNumberOne"));
  }

  @Test
  public void testAttemptsToWithTwoParametersWithLogGroupingWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        "Group", "group description");

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(4));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number to initial number"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumberToInitialNumberOne"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));


  }

  @Test
  public void testAttemptsToWithThreeParametersWithLogGroupingWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        "Group", "group description");

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(7));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number to initial number"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumberToInitialNumberOne"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithFourParametersWithLogGroupingWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        "Group", "group description");

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(11));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number to initial number"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumberToInitialNumberOne"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithFiveParametersWithLogGroupingWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$(
        AddNumberToInitialNumberOne.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        "Group", "group description");

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(16));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number to initial number"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumberToInitialNumberOne"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithSixParametersWithLogGroupingWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$(
      AddNumberToInitialNumberOne.of(1),
      AddNumber.of(2),
      AddNumber.of(3),
      AddNumber.of(4),
      AddNumber.of(5),
      AddNumber.of(6),
      "Group", "group description");

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(22));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number to initial number"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumberToInitialNumberOne"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));

    assertThat("group contains task 6",
      log.activityNodes.get(0).activityNodes.get(5).description, equalTo("add number 6 to given number 16"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(5).name, equalTo("AddNumber"));
  }


  @Test
  public void testAttemptsToWithSevenParametersWithLogGroupingWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$(
      AddNumberToInitialNumberOne.of(1),
      AddNumber.of(2),
      AddNumber.of(3),
      AddNumber.of(4),
      AddNumber.of(5),
      AddNumber.of(6),
      AddNumber.of(7),
      "Group", "group description");

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(29));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number to initial number"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumberToInitialNumberOne"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));

    assertThat("group contains task 6",
      log.activityNodes.get(0).activityNodes.get(5).description, equalTo("add number 6 to given number 16"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(5).name, equalTo("AddNumber"));

    assertThat("group contains task 7",
      log.activityNodes.get(0).activityNodes.get(6).description, equalTo("add number 7 to given number 22"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(6).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithEightParametersWithLogGroupingWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$(
      AddNumberToInitialNumberOne.of(1),
      AddNumber.of(2),
      AddNumber.of(3),
      AddNumber.of(4),
      AddNumber.of(5),
      AddNumber.of(6),
      AddNumber.of(7),
      AddNumber.of(8),
      "Group", "group description");

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(37));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number to initial number"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumberToInitialNumberOne"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));

    assertThat("group contains task 6",
      log.activityNodes.get(0).activityNodes.get(5).description, equalTo("add number 6 to given number 16"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(5).name, equalTo("AddNumber"));

    assertThat("group contains task 7",
      log.activityNodes.get(0).activityNodes.get(6).description, equalTo("add number 7 to given number 22"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(6).name, equalTo("AddNumber"));

    assertThat("group contains task 8",
      log.activityNodes.get(0).activityNodes.get(7).description, equalTo("add number 8 to given number 29"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(7).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithNineParametersWithLogGroupingWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$(
      AddNumberToInitialNumberOne.of(1),
      AddNumber.of(2),
      AddNumber.of(3),
      AddNumber.of(4),
      AddNumber.of(5),
      AddNumber.of(6),
      AddNumber.of(7),
      AddNumber.of(8),
      AddNumber.of(9),
      "Group", "group description");

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(46));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number to initial number"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumberToInitialNumberOne"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));

    assertThat("group contains task 6",
      log.activityNodes.get(0).activityNodes.get(5).description, equalTo("add number 6 to given number 16"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(5).name, equalTo("AddNumber"));

    assertThat("group contains task 7",
      log.activityNodes.get(0).activityNodes.get(6).description, equalTo("add number 7 to given number 22"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(6).name, equalTo("AddNumber"));

    assertThat("group contains task 8",
      log.activityNodes.get(0).activityNodes.get(7).description, equalTo("add number 8 to given number 29"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(7).name, equalTo("AddNumber"));

    assertThat("group contains task 9",
      log.activityNodes.get(0).activityNodes.get(8).description, equalTo("add number 9 to given number 37"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(8).name, equalTo("AddNumber"));
  }

  @Test
  public void testAttemptsToWithTenParametersWithLogGroupingWithoutInput() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> number = actor.attemptsTo$(
      AddNumberToInitialNumberOne.of(1),
      AddNumber.of(2),
      AddNumber.of(3),
      AddNumber.of(4),
      AddNumber.of(5),
      AddNumber.of(6),
      AddNumber.of(7),
      AddNumber.of(8),
      AddNumber.of(9),
      AddNumber.of(10),
      "Group", "group description");

    assertThat("task execution is successful", number.isRight(), equalTo(true));
    assertThat("output is as expected", number.get(), equalTo(56));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains group",
      log.activityNodes.get(0).description, equalTo("group description"));
    assertThat("group has correct name", log.activityNodes.get(0).name, equalTo("Group"));

    assertThat("group contains task 1",
      log.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number to initial number"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumberToInitialNumberOne"));

    assertThat("group contains task 2",
      log.activityNodes.get(0).activityNodes.get(1).description, equalTo("add number 2 to given number 2"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("AddNumber"));

    assertThat("group contains task 3",
      log.activityNodes.get(0).activityNodes.get(2).description, equalTo("add number 3 to given number 4"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("AddNumber"));

    assertThat("group contains task 4",
      log.activityNodes.get(0).activityNodes.get(3).description, equalTo("add number 4 to given number 7"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("AddNumber"));

    assertThat("group contains task 5",
      log.activityNodes.get(0).activityNodes.get(4).description, equalTo("add number 5 to given number 11"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(4).name, equalTo("AddNumber"));

    assertThat("group contains task 6",
      log.activityNodes.get(0).activityNodes.get(5).description, equalTo("add number 6 to given number 16"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(5).name, equalTo("AddNumber"));

    assertThat("group contains task 7",
      log.activityNodes.get(0).activityNodes.get(6).description, equalTo("add number 7 to given number 22"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(6).name, equalTo("AddNumber"));

    assertThat("group contains task 8",
      log.activityNodes.get(0).activityNodes.get(7).description, equalTo("add number 8 to given number 29"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(7).name, equalTo("AddNumber"));

    assertThat("group contains task 9",
      log.activityNodes.get(0).activityNodes.get(8).description, equalTo("add number 9 to given number 37"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(8).name, equalTo("AddNumber"));

    assertThat("group contains task 10",
      log.activityNodes.get(0).activityNodes.get(9).description, equalTo("add number 10 to given number 46"));
    assertThat("task 1 has correct name", log.activityNodes.get(0).activityNodes.get(9).name, equalTo("AddNumber"));
  }

}
