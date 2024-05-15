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

    String expectedError = "Additional Error\ncaused By:\njava.lang.RuntimeException: RuntimeException";

    assertThat("check final error message", tryValue.getLeft().getMessage(), equalTo(expectedError));

    assertThat("check error is of type ActivityError", tryValue.getLeft() instanceof ActivityError);
  }
}
