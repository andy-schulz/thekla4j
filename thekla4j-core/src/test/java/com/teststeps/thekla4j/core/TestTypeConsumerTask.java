package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.activityLog.TheklaActivityLog;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import com.teststeps.thekla4j.core.tasks.ConsumeString;
import io.vavr.control.Either;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestTypeConsumerTask {

  private Actor tester;

  @BeforeEach
  public void setup() {
    tester = Actor.named("tester");
  }

  @AfterEach
  public void tearDown() {
    tester.cleansStage();
  }

  @Test
  public void runBasicConsumerTask() {
    Either<ActivityError, Void> result = tester.attemptsTo_(
        ConsumeString.print())
      .using("test");

    assertThat("execution of consumer task is successful", result.isRight(), equalTo(true));
  }

  @Test
  public void runBasicConsumerTaskFailing() {

    ConsumeString print = ConsumeString.print();

    assertThat("toString is task name", print.toString(), equalTo("ConsumeString"));

    Either<ActivityError, Void> result = tester.attemptsTo_(
        print)
      .using("throw");

    assertThat("execution of consumer task fails", result.isLeft(), equalTo(true));
  }

  @Test
  public void runBasicConsumerTaskWithInput() throws ActivityError {

    Either<ActivityError, Void> result = ConsumeString.print().runAs(tester, "test");

    assertThat("either is right", result.isRight(), equalTo(true));

    Either<ActivityError, Void> fails = ConsumeString.print().runAs(tester, "throw");

    assertThat("either is left", fails.isLeft(), equalTo(true));

  }

  @Test
  public void runBasicConsumerTaskWithAttemptsWith() {

    Either<ActivityError, Void> result = ConsumeString.print().runAs(tester).using("test");

    assertThat("either is right", result.isRight(), equalTo(true));

    Either<ActivityError, Void> fails = ConsumeString.print().runAs(tester).using("throw");

    assertThat("either is left", fails.isLeft(), equalTo(true));
  }

  @Test
  public void runBasicConsumerTaskRunAs$AllParameter() {

    Either<ActivityError, Void> result = ConsumeString.print().runAs$(tester, "test", "group", "description");

    assertThat("either is right", result.isRight(), equalTo(true));

    TheklaActivityLog log = tester.activityLog;
    ActivityLogNode lastLog = log.getLogTree();

    assertThat("group name is correct", lastLog.activityNodes.get(0).name, equalTo("group"));
    assertThat("description is correct", lastLog.activityNodes.get(0).description, equalTo("description"));

    assertThat("task group name is correct", lastLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("ConsumeString"));
    assertThat("task description is correct", lastLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Consume a string"));
  }

  @Test
  public void runBasicConsumerTaskRunAs$LogAnnotator() {

    Either<ActivityError, Void> result = ConsumeString.print()
      .runAs$(tester, "test")
      .annotate("group", "description");

    assertThat("either is right", result.isRight(), equalTo(true));

    TheklaActivityLog log = tester.activityLog;
    ActivityLogNode lastLog = log.getLogTree();

    assertThat("group name is correct", lastLog.activityNodes.get(0).name, equalTo("group"));
    assertThat("description is correct", lastLog.activityNodes.get(0).description, equalTo("description"));

    assertThat("task group name is correct", lastLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("ConsumeString"));
    assertThat("task description is correct", lastLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Consume a string"));
  }

  @Test
  public void runBasicConsumerTaskRunAs$LogAnnotatorAttemptsWith() {

    Either<ActivityError, Void> result = ConsumeString.print()
      .runAs$(tester)
      .annotate("group", "description")
      .using("test");

    assertThat("either is right", result.isRight(), equalTo(true));

    TheklaActivityLog log = tester.activityLog;
    ActivityLogNode lastLog = log.getLogTree();

    assertThat("group name is correct", lastLog.activityNodes.get(0).name, equalTo("group"));
    assertThat("description is correct", lastLog.activityNodes.get(0).description, equalTo("description"));

    assertThat("task group name is correct", lastLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("ConsumeString"));
    assertThat("task description is correct", lastLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Consume a string"));
  }

  @Test
  public void runBasicConsumerTaskWithExceptionRunAsPerformer() throws ActivityError {

    ConsumeString.print().runAs(Performer.of(tester), "test");

    assertThrows(
      ActivityError.class,
      () -> ConsumeString.print().runAs(Performer.of(tester), "throw"));
  }

  @Test
  public void runBasicConsumerTaskWithExceptionRunAsPerformerAttemptsWith() throws ActivityError {

    ConsumeString.print()
      .runAs(Performer.of(tester))
      .using("test");

    assertThrows(
      ActivityError.class,
      () -> ConsumeString.print().runAs(Performer.of(tester), "throw"));
  }

  @Test
  public void runBasicConsumerTaskWithExceptionRunAs$Performer() throws ActivityError {

    ConsumeString.print().runAs$(Performer.of(tester), "test", "group", "description");

    TheklaActivityLog log = tester.activityLog;
    ActivityLogNode lastLog = log.getLogTree();

    assertThat("group name is correct", lastLog.activityNodes.get(0).name, equalTo("group"));
    assertThat("description is correct", lastLog.activityNodes.get(0).description, equalTo("description"));

    assertThat("task group name is correct", lastLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("ConsumeString"));
    assertThat("task description is correct", lastLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Consume a string"));
  }

  @Test
  public void runBasicConsumerTaskWithExceptionRunAs$PerformerLogAnnotator() throws ActivityError {

    ConsumeString.print().runAs$(Performer.of(tester), "test").annotate("group", "description");

    TheklaActivityLog log = tester.activityLog;
    ActivityLogNode lastLog = log.getLogTree();

    assertThat("group name is correct", lastLog.activityNodes.get(0).name, equalTo("group"));
    assertThat("description is correct", lastLog.activityNodes.get(0).description, equalTo("description"));

    assertThat("task group name is correct", lastLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("ConsumeString"));
    assertThat("task description is correct", lastLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Consume a string"));
  }

  @Test
  public void runBasicConsumerTaskWithExceptionRunAs$PerformerLogAnnotatorAttemptsWith() throws ActivityError {

    ConsumeString.print()
      .runAs$(Performer.of(tester))
      .annotate("group", "description")
      .using("test");

    TheklaActivityLog log = tester.activityLog;
    ActivityLogNode lastLog = log.getLogTree();

    assertThat("group name is correct", lastLog.activityNodes.get(0).name, equalTo("group"));
    assertThat("description is correct", lastLog.activityNodes.get(0).description, equalTo("description"));

    assertThat("task group name is correct", lastLog.activityNodes.get(0).activityNodes.get(0).name, equalTo("ConsumeString"));
    assertThat("task description is correct", lastLog.activityNodes.get(0).activityNodes.get(0).description, equalTo("Consume a string"));
  }
}

