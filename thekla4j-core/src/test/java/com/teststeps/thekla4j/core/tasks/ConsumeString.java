package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.ConsumerTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

import java.util.Objects;

public class ConsumeString extends ConsumerTask<String> {

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor, String input) {
    if(Objects.equals(input, "throw"))
      return Either.left(ActivityError.of("PrintString: input is 'throw'"));
    else  {
      return Either.right(null);
    }
  }


  public static ConsumeString print() {
    return new ConsumeString();
  }

}
