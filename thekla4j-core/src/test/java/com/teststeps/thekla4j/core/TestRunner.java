package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import static com.teststeps.thekla4j.core.activities.API.run;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestRunner {

  @Test
  void testRunnerFunction() {
    Actor tester = Actor.named("Tester");
    String input = "test";


    Either<ActivityError, String> result = tester.attemptsTo(
        StringTask.of(input),
        run(StringTask::of));

    assertThat("either is right", result.isRight(), equalTo(true));
    assertThat("result is as expected", result.get(), equalTo(input));

  }

  @AllArgsConstructor
  static class StringTask extends Task<Void, String> {
    String result;

    @Override
    protected Either<ActivityError, String> performAs(Actor actor, Void unused) {
      return Either.right(result);
    }

    public static StringTask of(String result) {
      return new StringTask(result);
    }
  }
}
