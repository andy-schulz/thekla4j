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

    String expectedError = "Additional Error\ncaused By:\nRuntimeException";

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
    assertThat("check error message", error.getMessage(), equalTo("Error Message\ncaused By:\nRuntimeException"));
    assertThat("check error cause", error.getCause().getMessage(), equalTo("RuntimeException"));
  }

  @Test
  public void createActivityErrorWithUtilityMethodWithAndCauseAndAdditionalMessage() {

    ActivityError error = ActivityError.with(new RuntimeException("RuntimeException"));
    assertThat("check error message", error.getMessage(), equalTo("RuntimeException"));
    assertThat("check error cause", error.getCause(), equalTo(null));
  }

}
