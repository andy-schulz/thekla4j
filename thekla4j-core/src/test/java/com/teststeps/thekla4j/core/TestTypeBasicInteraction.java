package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.activityLog.TheklaActivityLog;
import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestTypeBasicInteraction {

  @Test
  public void testBasicInteraction() {
    Actor actor = Actor.named("TestActor");

    List<String> testList = new ArrayList<>();

    BasicInteractionTask task = BasicInteractionTask.start(testList);

    assertThat("get name of task", task.toString(), equalTo("BasicInteractionTask"));

    Either<ActivityError, Void> result = task.performAs(actor);

    assertThat("task execution is successful", result.isRight());
  }

  @Test
  public void failingBasicInteractionTask() {
    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Void> result = FailingBasicInteractionTask.start().performAs(actor);

    assertThat("task execution is not successful", result.isLeft(), equalTo(true));
  }


  @Test
  public void failingBasicInteractionTaskThrowing() throws ActivityError {
    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Void> error = FailingBasicInteractionTask.start().runAs(actor);

    assertThat("task execution is not successful", error.isLeft(), equalTo(true));
    assertThat("task execution is not successful", error.getLeft().getMessage(), equalTo("test"));
  }

  @Test
  public void testBasicInteractionWithRunMethod() throws ActivityError {
    Actor actor = Actor.named("TestActor");

    List<String> testList = new ArrayList<>();

    BasicInteractionTask.start(testList).runAs(actor);

    assertThat("list was changed during execution", testList.size(), equalTo(1));
    assertThat("test was added to test list during execution", testList.get(0), equalTo("task executed"));
  }

  @Test
  public void testBasicInteractionWithRunAs$Method() throws ActivityError {
    Actor actor = Actor.named("TestActor");

    List<String> testList = new ArrayList<>();

    BasicInteractionTask.start(testList).runAs$(actor, "myGroup", "myDescription");

    TheklaActivityLog log = actor.activityLog;
    ActivityLogNode rootLog = log.getLogTree();

    assertThat("list was changed during execution", testList.size(), equalTo(1));
    assertThat("test was added to test list during execution", testList.get(0), equalTo("task executed"));


    assertThat("group was added to log", rootLog.activityNodes.get(0).name, equalTo("myGroup"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).description, equalTo("myDescription"));

    assertThat("group was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("BasicInteractionTask"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Basic Interaction Task"));

  }

  @Test
  public void testBasicInteractionWithRunAs$Annotator() throws ActivityError {
    Actor actor = Actor.named("TestActor");

    List<String> testList = new ArrayList<>();

    BasicInteractionTask.start(testList)
      .runAs$(actor)
      .annotate("myGroup", "myDescription");

    TheklaActivityLog log = actor.activityLog;
    ActivityLogNode rootLog = log.getLogTree();

    assertThat("list was changed during execution", testList.size(), equalTo(1));
    assertThat("test was added to test list during execution", testList.get(0), equalTo("task executed"));


    assertThat("group was added to log", rootLog.activityNodes.get(0).name, equalTo("myGroup"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).description, equalTo("myDescription"));

    assertThat("group was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("BasicInteractionTask"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Basic Interaction Task"));

  }

  @Test
  public void testBasicInteractionWithRunMethodAsPerformer() throws ActivityError {
    Actor actor = Actor.named("TestActor");
    List<String> testList = new ArrayList<>();

    BasicInteractionTask.start(testList).runAs(Performer.of(actor));

    assertThat("list was changed during execution", testList.size(), equalTo(1));
    assertThat("test was added to test list during execution", testList.get(0), equalTo("task executed"));
  }

  @Test
  public void testBasicInteractionWithRunAs$MethodPerformer() throws ActivityError {
    Actor actor = Actor.named("TestActor");
    List<String> testList = new ArrayList<>();

    BasicInteractionTask.start(testList).runAs$(Performer.of(actor), "myGroup", "myDescription");

    TheklaActivityLog log = actor.activityLog;
    ActivityLogNode rootLog = log.getLogTree();

    assertThat("list was changed during execution", testList.size(), equalTo(1));
    assertThat("test was added to test list during execution", testList.get(0), equalTo("task executed"));

    assertThat("group was added to log", rootLog.activityNodes.get(0).name, equalTo("myGroup"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).description, equalTo("myDescription"));

    assertThat("group was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("BasicInteractionTask"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Basic Interaction Task"));
  }

  @Test
  public void testBasicInteractionWithRunAs$AnnotatorPerformer() throws ActivityError {
    Actor actor = Actor.named("TestActor");
    List<String> testList = new ArrayList<>();

    BasicInteractionTask
      .start(testList)
      .runAs$(Performer.of(actor))
      .annotate( "myGroup", "myDescription");

    TheklaActivityLog log = actor.activityLog;
    ActivityLogNode rootLog = log.getLogTree();

    assertThat("list was changed during execution", testList.size(), equalTo(1));
    assertThat("test was added to test list during execution", testList.get(0), equalTo("task executed"));

    assertThat("group was added to log", rootLog.activityNodes.get(0).name, equalTo("myGroup"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).description, equalTo("myDescription"));

    assertThat("group was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("BasicInteractionTask"));
    assertThat("description was added to log", rootLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Basic Interaction Task"));
  }



  @Test
  @DisplayName("calling perform on a BasicInteraction with null actor should throw exception")
  public void testUsingANullActor() {

    List<String> testList = new ArrayList<>();

    Throwable thrown = assertThrows(
      NullPointerException.class,
      () -> BasicInteractionTask.start(testList).runAs((Actor) null));

    assertThat(thrown.getMessage(), startsWith("actor is marked non-null but is null"));
  }

  @Test
  public void testEvaluation() {
    Actor actor = Actor.named("TestActor");

    List<String> testList = new ArrayList<>();

    BasicInteractionTask task = BasicInteractionTask.start(testList);

    Either<ActivityError, Void> result = actor.attemptsTo(task);

    assertThat("task execution is successful", result.isRight());
    assertThat("list was changed during execution", testList.size(), equalTo(1));
    assertThat("test was added to test list during execution", testList.get(0), equalTo("task executed"));
    assertThat("task is evaluated", result.get(), equalTo(null));
  }



}

@AllArgsConstructor
@Action("Basic Interaction Task")
class BasicInteractionTask extends BasicInteraction {

  private List<String> testList;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    testList.add("task executed");
    return Either.right(null);
  }

  public static BasicInteractionTask start(List<String> testList) {
    return new BasicInteractionTask(testList);
  }
}


class FailingBasicInteractionTask extends BasicInteraction {

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return Either.left(ActivityError.of("test"));
  }

  public static FailingBasicInteractionTask start() {
    return new FailingBasicInteractionTask();
  }
}
