package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.activityLog.TheklaActivityLog;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import com.teststeps.thekla4j.core.tasks.AddNumber;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TestTypeTask {

  @Test
  @DisplayName("calling runAs on a task with null actor should throw exception")
  public void testUsingRunAsMethod() {

    Throwable thrown = assertThrows(
      NullPointerException.class,
      () -> AddNumber.of(1).runAs((Actor) null, 1));

    assertThat(thrown.getMessage(), startsWith("actor is marked non-null but is null"));
  }

  @Test
  public void testResultForRunAsMethodWithActor() throws ActivityError {

    Actor actor = Actor.named("TheActor");

    AddNumber task = AddNumber.of(1);

    Either<ActivityError, Integer> result =  task.runAs(actor, 2);

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("output is as expected", result.get(), equalTo(3));

  }

  @Test
  public void testResultForRunAsMethodWithActorAttemptsWith() throws ActivityError {

    Actor actor = Actor.named("TheActor");

    AddNumber task = AddNumber.of(1);

    Either<ActivityError, Integer> result =  task.runAs(actor).using(2);

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("output is as expected", result.get(), equalTo(3));

  }

  @Test
  public void testResultForRunAs$MethodWithActor() throws ActivityError {

    Actor actor = Actor.named("TheActor");

    AddNumber task = AddNumber.of(1);

    Either<ActivityError, Integer> result =  task.runAs$(actor, 2, "group", "description");

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("output is as expected", result.get(), equalTo(3));

    TheklaActivityLog log = actor.activityLog;
    ActivityLogNode rootNode = log.getLogTree();

    assertThat("log group is set", rootNode.activityNodes.get(0).name, equalTo("group"));
    assertThat("log description is set", rootNode.activityNodes.get(0).description, equalTo("description"));

    assertThat("log node name is correct", rootNode.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));
    assertThat("log node task description is correct", rootNode.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 2"));

  }

  @Test
  public void testResultForRunAs$MethodWithActorLogAnnotator() throws ActivityError {

    Actor actor = Actor.named("TheActor");

    AddNumber task = AddNumber.of(1);

    Either<ActivityError, Integer> result =  task.runAs$(actor, 2).annotate("group", "description");

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("output is as expected", result.get(), equalTo(3));

    TheklaActivityLog log = actor.activityLog;
    ActivityLogNode rootNode = log.getLogTree();

    assertThat("log group is set", rootNode.activityNodes.get(0).name, equalTo("group"));
    assertThat("log description is set", rootNode.activityNodes.get(0).description, equalTo("description"));

    assertThat("log node name is correct", rootNode.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));
    assertThat("log node task description is correct", rootNode.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 2"));

  }

  @Test
  public void testResultForRunAs$MethodWithActorLogAnnotatorAttemptsWith() throws ActivityError {

    Actor actor = Actor.named("TheActor");

    AddNumber task = AddNumber.of(1);

    Either<ActivityError, Integer> result =  task.runAs$(actor).annotate("group", "description").using(2);

    assertThat("result is right", result.isRight(), equalTo(true));
    assertThat("output is as expected", result.get(), equalTo(3));

    TheklaActivityLog log = actor.activityLog;
    ActivityLogNode rootNode = log.getLogTree();

    assertThat("log group is set", rootNode.activityNodes.get(0).name, equalTo("group"));
    assertThat("log description is set", rootNode.activityNodes.get(0).description, equalTo("description"));

    assertThat("log node name is correct", rootNode.activityNodes.get(0).activityNodes.get(0).name, equalTo("AddNumber"));
    assertThat("log node task description is correct", rootNode.activityNodes.get(0).activityNodes.get(0).description, equalTo("add number 1 to given number 2"));

  }

  @Test
  public void testResultForRunAsMethodWithPerformer() throws ActivityError {

    Performer performer = Performer.of(Actor.named("TheActor"));

    AddNumber task = AddNumber.of(1);

    Integer result =  task.runAs(performer, 2);

    assertThat("output is as expected", result, equalTo(3));

  }

  @Test
  public void testResultForRunAsMethodWithPerformerAttemptsWith() throws ActivityError {

    Performer performer = Performer.of(Actor.named("TheActor"));

    AddNumber task = AddNumber.of(1);

    Integer result =  task.runAs(performer).using(3);

    assertThat("output is as expected", result, equalTo(4));

  }


  @Test
  public void testResultForRunAs$MethodPerformerWithActorLogAnnotatorAttemptsWith() throws ActivityError {

    Performer performer = Performer.of(Actor.named("TheActor"));

    AddNumber task = AddNumber.of(2);

    Integer result =  task.runAs$(performer).annotate("group", "description").using(3);

    assertThat("output is as expected", result, equalTo(5));

  }

  @Test
  public void testResultForRunAs$MethodPerformerWithActorLogAnnotator() throws ActivityError {

    Performer performer = Performer.of(Actor.named("TheActor"));

    AddNumber task = AddNumber.of(2);

    Integer result =  task.runAs$(performer, 4).annotate("group", "description");

    assertThat("output is as expected", result, equalTo(6));

  }

  @Test
  public void testActorString() {

    Actor actor = Actor.named("TheActor");

    Task<Integer, Integer> task = AddNumber.of(1);

    Either<ActivityError, Integer> result = actor.attemptsTo_(task).using(2);

    String str = task.toString();

    assertThat("task execution is successful", result.isRight());
    assertThat("to string is class name", str, equalTo("AddNumber"));
  }


}
