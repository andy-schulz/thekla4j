package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.teststeps.thekla4j.activityLog.TheklaActivityLog;
import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestTypeInteraction {

  @Test
  public void testInteraction() {
    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Integer> result = InteractionTask.start().performAs(actor, 2);

    assertThat("task execution is successful", result.isRight());
    assertThat("output is as expected", result.get(), equalTo(2));
  }

  @Test
  @DisplayName("calling perform on an Interaction with null actor should throw exception")
  public void testUsingANullActor() {

    Throwable thrown = assertThrows(
      NullPointerException.class,
      () -> InteractionTask.start().runAs((Actor) null, null));

    assertThat(thrown.getMessage(), startsWith("actor is marked non-null but is null"));
  }

  @Test
  public void testEvaluation() {
    Actor actor = Actor.named("TestActor");

    InteractionTask task = InteractionTask.start();

    Either<ActivityError, Integer> result = actor.attemptsTo_(task).using(3);


    assertThat("task execution is successful", result.isRight());
    assertThat("task is evaluated", result.get(), equalTo(3));
  }

  @Action("Interaction Task")
  static class InteractionTask extends Interaction<Integer, Integer> {

    @Override
    protected Either<ActivityError, Integer> performAs(Actor actor, Integer integer) {
      return Either.right(integer);
    }

    public static InteractionTask start() {
      return new InteractionTask();
    }
  }

  @Test
  public void runBasicSupplierTaskWithRunMethod() throws ActivityError {
    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Integer> result = InteractionTask.start().runAs(actor, 2);

    assertThat("result is RIGHT", result.isRight(), equalTo(true));
    assertThat("result is correct", result.get(), equalTo(2));

  }

  @Test
  public void runBasicInteractionWithRunAs$Method() {
    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Integer> result = InteractionTask.start().runAs$(actor, 2, "group", "description");

    TheklaActivityLog log = actor.activityLog;
    ActivityLogNode rootLog = log.getLogTree();

    assertThat("result is RIGHT", result.isRight(), equalTo(true));
    assertThat("result is correct", result.get(), equalTo(2));

    assertThat("group was added to log", rootLog.activityNodes.get(0).name, equalTo("group"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).description, equalTo("description"));

    assertThat("group was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("InteractionTask"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Interaction Task"));
  }

  @Test
  public void runBasicInteractionWithRunAs$WithAnnotate() {
    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Integer> result = InteractionTask.start()
        .runAs$(actor, 2)
        .annotate("group", "description");

    TheklaActivityLog log = actor.activityLog;
    ActivityLogNode rootLog = log.getLogTree();

    assertThat("result is RIGHT", result.isRight(), equalTo(true));
    assertThat("result is correct", result.get(), equalTo(2));

    assertThat("group was added to log", rootLog.activityNodes.get(0).name, equalTo("group"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).description, equalTo("description"));

    assertThat("group was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("InteractionTask"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Interaction Task"));
  }

  @Test
  public void runBasicSupplierTaskWithRunMethodAsPerformer() throws ActivityError {
    Actor actor = Actor.named("TestActor");

    Integer result = InteractionTask.start().runAs(Performer.of(actor), 3);

    assertThat("result is correct", result, equalTo(3));

  }

  @Test
  public void runBasicSupplierTaskWithRunMethodAsPerformer$() throws ActivityError {
    Actor actor = Actor.named("TestActor");

    Integer result = InteractionTask.start().runAs$(Performer.of(actor), 3, "group", "description");

    TheklaActivityLog log = actor.activityLog;
    ActivityLogNode rootLog = log.getLogTree();

    assertThat("result is correct", result, equalTo(3));

    assertThat("group was added to log", rootLog.activityNodes.get(0).name, equalTo("group"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).description, equalTo("description"));

    assertThat("group was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("InteractionTask"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Interaction Task"));
  }
}
