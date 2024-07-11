package com.teststeps.thekla4j.commons;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ActivityErrorTest {

  @Test
  public void testActivityErrorToEither() {

    Either<ActivityError, Object> tryValue = Try.failure(new RuntimeException("RuntimeException"))
      .transform(ActivityError.toEither("Additional Error"));

    String expectedError = "Additional Error";

    assertThat("check final error message", tryValue.getLeft().getMessage(), equalTo(expectedError));

    assertThat("check error is of type ActivityError", tryValue.getLeft().getClass().getSimpleName(), equalTo("ActivityError") );
  }

  @Test
  public void testActivityErrorToSuccessfulEither() {

      Either<ActivityError, String> tryValue = Try.success("Success")
        .transform(ActivityError.toEither("Additional Error"));

      assertThat("check final value", tryValue.get(), equalTo("Success"));
  }

  @Test
  public void createActivityErrorWithUtilityMethodWith() {

    ActivityError error = ActivityError.with("Error Message");
    assertThat("check error message", error.getMessage(), equalTo("Error Message"));
  }

  @Test
  public void createActivityErrorWithUtilityMethodWithAndCause() {

    ActivityError error = ActivityError.with("Error Message", new RuntimeException("RuntimeException"));
    assertThat("check error message", error.getMessage(), equalTo("Error Message"));
    assertThat("check error cause", error.getCause().getMessage(), equalTo("RuntimeException"));
  }

  @Test
  public void createActivityErrorWithUtilityMethodWithAndCauseAndAdditionalMessage() {

    ActivityError error = ActivityError.with(new RuntimeException("RuntimeException"));
    assertThat("check error message", error.getMessage(), equalTo("RuntimeException"));
    assertThat("check error cause", error.getCause(), equalTo(null));
  }

  @Test
  public void testActivityErrorMessageAggregation() {
    ActivityError error3 = ActivityError.with("Error Message 3");
    ActivityError error2 = ActivityError.with("Error Message 2", error3);
    ActivityError error = ActivityError.with("Error Message 1", error2);

    ActivityError aggregatedError = error.aggregateMessages();

    assertThat("check aggregated error messages", aggregatedError.getMessage(), equalTo("Error Message 1\ncaused By:\nError Message 2\ncaused By:\nError Message 3"));
    assertThat("check error cause", aggregatedError.getCause().getMessage(), equalTo("Error Message 1"));
  }

}
