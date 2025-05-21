package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.data.UserDataRecord;
import io.vavr.control.Either;

@Workflow("task with user data name: '@{userName}'")
public class TaskWithRecordData extends Task<Void, String> {

  @Called(name = "userName", value = "name")
  private final UserDataRecord userData = UserDataRecord.standard();
//
//  @Called(name ="userName", value = "name")
//  private final UserDataClass userData = UserDataClass.standard();


  @Override
  protected Either<ActivityError, String> performAs(Actor actor, Void result) {
    System.out.println(userData);
    return Either.right("nothing");
  }

  public static TaskWithRecordData start() {
    return new TaskWithRecordData();
  }
}
