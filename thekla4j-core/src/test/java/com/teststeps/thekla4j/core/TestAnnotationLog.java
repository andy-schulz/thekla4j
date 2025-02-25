package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activityLog.ProcessLogAnnotation;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.AddNumber;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestAnnotationLog {

  @Test
  void testDoubleLogAnnotation() {
    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Integer> result = actor.attemptsTo(DoubleParameterLogAnnotation.with(1));

    assertThat("task execution is successful", result.isRight());
    assertThat("output is as expected", result.get(), equalTo(2));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("log contains first parameter", log.activityNodes.get(0).description, equalTo("double log annotation parameter 1 and 1"));
  }

  @Test
  void testDoubleInputLogAnnotation() {
    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Integer> result = actor.attemptsTo_(DoubleInputLogAnnotation.start()).using(2);

    assertThat("task execution is successful", result.isRight());
    assertThat("output is as expected", result.get(), equalTo(3));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("log contains first parameter", log.activityNodes.get(0).description, equalTo("double log annotation input 2 and 2"));
  }

  @Test
  void testNotExistingLogParameterInAnnotations() {
    Actor actor = Actor.named("Tester");

    Either<ActivityError, Integer> result =
      actor.attemptsTo_(
        MissingLogAnnotationParameter.start())
        .using(2);

    // task is successful even when the value does not exist
    assertThat("task execution is successful", result.isRight());
    assertThat("output is as expected", result.get(), equalTo(3));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("log contains first parameter", log.activityNodes.get(0).description, equalTo("missing log annotation @{two}"));
  }

  @Test
  void testPackagePrivateAnnotationParameter() {
    Actor actor = Actor.named("Tester");

    Either<ActivityError, Void> result =
      actor.attemptsTo(
          PackagePrivateAnnotationParameter.start(1));

    assertThat("task execution is successful", result.isRight());
    assertThat("output is as expected", result.get(), equalTo(null));

    ActivityLogNode log = actor.activityLog.getLogTree();
    assertThat("log contains first parameter", log.activityNodes.get(0).description, equalTo("package private parameter 1"));
  }

  @Test
  void testProtectedAnnotationParameter() {
    Actor actor = Actor.named("Tester");

    Either<ActivityError, Void> result =
      actor.attemptsTo(
        ProtectedAnnotationParameter.start(1));

    assertThat("task execution is successful", result.isRight());
    assertThat("output is as expected", result.get(), equalTo(null));

    ActivityLogNode log = actor.activityLog.getLogTree();
    assertThat("log contains first parameter", log.activityNodes.get(0).description, equalTo("protected parameter Error: Cant access field integer"));
  }

  @Test
  public void testLogAnnotationProcessingWithNullActor() {


    Try<ProcessLogAnnotation.ActivityLogData> data =
      ProcessLogAnnotation.forActivity(AddNumber.of(1)).withParameter(1).andActor(null);

    assertThat("data is a failure", data.isFailure());
    assertThat("correct description is thrown", data.getCause().getMessage(), startsWith("Property  ProcessLogAnnotation::actor is null, but should not."));
  }

  @Test
  public void testLogAnnotationProcessingWithNullActivity() {

    Actor actor = Actor.named("TestActor");

    Try<ProcessLogAnnotation.ActivityLogData> data =
      ProcessLogAnnotation.forActivity(null).withParameter(1).andActor(actor);

    assertThat("data is a failure", data.isFailure());
    assertThat("correct description is thrown", data.getCause().getMessage(), startsWith("Property  ProcessLogAnnotation::activity is null, but should not."));
  }

  @Test
  @DisplayName("a @Called annotation on an actor is ignored")
  public void testAnnotationOnActor() {

    Actor actor = Actor.named("TestActor");

    Try<ProcessLogAnnotation.ActivityLogData> data =
      ProcessLogAnnotation.forActivity(AnnotationOnActor.start(1)).withParameter(2).andActor(actor);

    assertThat("data is a success", data.isSuccess(), equalTo(true));
    assertThat("correct description is thrown", data.get().description, startsWith("parameter: 1, input: 2,  actor: @{name}"));
  }

  @Workflow("double log annotation parameter @{one} and @{two}")
  static class DoubleParameterLogAnnotation extends Task<Void, Integer> {

    @Called(name = "one")
    @Called(name = "two")
    private final Integer one;

    @Override
    protected Either<ActivityError, Integer> performAs(Actor actor, Void unused) {
      return Either.right(one + 1);
    }

    public static DoubleParameterLogAnnotation with(Integer one) {
      return new DoubleParameterLogAnnotation(one);
    }

    private DoubleParameterLogAnnotation(Integer one) {
      this.one = one;
    }
  }

  @Workflow("double log annotation input @{one} and @{two}")
  static class DoubleInputLogAnnotation extends Task<Integer, Integer> {

    @Override
    protected Either<ActivityError, Integer> performAs(
      Actor actor,
      @Called(name = "one")
      @Called(name = "two")
      Integer integer
                                                      ) {
      return Either.right(integer + 1);
    }

    public static DoubleInputLogAnnotation start() {
      return new DoubleInputLogAnnotation();
    }
  }

  @Workflow("missing log annotation @{two}")
  static class MissingLogAnnotationParameter extends Task<Integer, Integer> {

    @Override
    protected Either<ActivityError, Integer> performAs(Actor actor, @Called(name = "one", value = "test") Integer integer) {
      return Either.right(integer + 1);
    }

    public static MissingLogAnnotationParameter start() {
      return new MissingLogAnnotationParameter();
    }
  }

  @Workflow("package private parameter @{integer}")
  static class PackagePrivateAnnotationParameter extends Task<Void, Void> {

    @Called(name = "integer")
    Integer integer;

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor, Void unused) {
      return Either.right(null);
    }

    public static PackagePrivateAnnotationParameter start(Integer integer) {
      return new PackagePrivateAnnotationParameter(integer);
    }

    private PackagePrivateAnnotationParameter(Integer integer) {
      this.integer = integer;
    }
  }

  @Workflow("protected parameter @{integer}")
  static class ProtectedAnnotationParameter extends Task<Void, Void> {

    @Called(name = "integer")
    protected Integer integer;

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor, Void unused) {
      return Either.right(null);
    }

    public static ProtectedAnnotationParameter start(Integer integer) {
      return new ProtectedAnnotationParameter(integer);
    }

    private ProtectedAnnotationParameter(Integer integer) {
      this.integer = integer;
    }
  }

  @Workflow("parameter: @{integer}, input: @{input},  actor: @{name}")
  static class AnnotationOnActor extends Task<Integer, Void> {

    @Called(name = "integer")
    private Integer integer;

    @Override
    protected Either<ActivityError, Void> performAs(@Called(name = "name", value = "name") Actor actor, @Called(name = "input") Integer input) {
      return Either.right(null);
    }

    public static AnnotationOnActor start(Integer integer) {
      return new AnnotationOnActor(integer);
    }

    private AnnotationOnActor(Integer integer) {
      this.integer = integer;
    }
  }



}
