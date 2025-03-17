package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.activityLog.TheklaActivityLog;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import com.teststeps.thekla4j.core.tasks.SupplyString;
import io.vavr.control.Either;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestTypeSupplierTask {

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
  public void runBasicSupplierTask() {
    Either<ActivityError, String> result = tester.attemptsTo(
        SupplyString.shallThrow(false));

    assertThat("execution of consumer task is successful", result.isRight(), equalTo(true));
    assertThat("result is correct", result.get(), equalTo("Hello World"));

  }

  @Test
  public void runBasicSupplierTaskWithRunMethod() throws ActivityError {

    String result = SupplyString.shallThrow(false).runAs(tester);

    assertThat("result is correct", result, equalTo("Hello World"));

  }

  @Test
  public void runBasicSupplierTaskWithRunAs$Method() throws ActivityError {

    String result = SupplyString.shallThrow(false).runAs$(tester, "group", "description");

    TheklaActivityLog log = tester.activityLog;
    ActivityLogNode node = log.getLogTree();

    assertThat("result is correct", result, equalTo("Hello World"));

    assertThat("log node is correct", node.activityNodes.get(0).name, equalTo("group"));
    assertThat("log node description is correct", node.activityNodes.get(0).description, equalTo("description"));

    assertThat("log node is correct", node.activityNodes.get(0).activityNodes.get(0).name, equalTo("SupplyString"));
    assertThat("log node description is correct", node.activityNodes.get(0).activityNodes.get(0).description, equalTo("Supply a string"));

  }

  @Test
  public void runBasicSupplierTaskWithRunMethodAsPerformer() throws ActivityError {

    String result = SupplyString.shallThrow(false).runAs(Performer.of(tester));

    assertThat("result is correct", result, equalTo("Hello World"));

  }

  @Test
  public void runBasicSupplierTaskWithRunMethodAs$Performer() throws ActivityError {

    String result = SupplyString.shallThrow(false).runAs$(Performer.of(tester), "group", "description");

    TheklaActivityLog log = tester.activityLog;
    ActivityLogNode node = log.getLogTree();

    assertThat("result is correct", result, equalTo("Hello World"));

    assertThat("log node is correct", node.activityNodes.get(0).name, equalTo("group"));
    assertThat("log node description is correct", node.activityNodes.get(0).description, equalTo("description"));

    assertThat("log node is correct", node.activityNodes.get(0).activityNodes.get(0).name, equalTo("SupplyString"));
    assertThat("log node description is correct", node.activityNodes.get(0).activityNodes.get(0).description, equalTo("Supply a string"));

  }

  @Test
  public void runBasicConsumerTaskWithException() throws ActivityError {

    assertThrows(
      ActivityError.class,
      () -> SupplyString.shallThrow(true).runAs(tester));

  }

  @Test
  public void runBasicConsumerTaskWithExceptionAsPerformer() throws ActivityError {

    assertThrows(
      ActivityError.class,
      () -> SupplyString.shallThrow(true).runAs(Performer.of(tester)));

  }
}
