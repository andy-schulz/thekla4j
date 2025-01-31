package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.ConsumeString;
import io.vavr.control.Either;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestConsumerTask {

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
      .apply("test");

    assertThat("execution of consumer task is successful", result.isRight(), equalTo(true));

  }

  @Test
  public void runBasicConsumerTaskWithException() throws ActivityError {

    ConsumeString.print().runAs(tester, "test");

    assertThrows(
      ActivityError.class,
      () -> ConsumeString.print().runAs(tester, "throw"));


  }
}
