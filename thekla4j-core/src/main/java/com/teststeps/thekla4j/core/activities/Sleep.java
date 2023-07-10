package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function0;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

@Log4j2(topic = "Activity Sleep")
@Action("pause all activities for @{duration} @{reason}")
public class Sleep<T> extends Interaction<T, T> {

  private Duration duration;

  @Called(name = "duration")
  private Function0<String> durationText = () -> "" + duration.getSeconds() + " Seconds (" + duration.toMillis() + " ms)";


  private String reason = "";

  @Called(name = "reason")
  private Function0<String> getReason = () -> reason.equals("") ? "" : "with reason '" + reason + "'";


  /**
   * @param actor  the actor performing the action
   * @param result the passed result will just be passed through
   * @return - the
   */
  @Override
  protected Either<ActivityError, T> performAs(Actor actor, T result) {
    return Try
        .of(() -> {
          Thread.sleep(duration.toMillis());
          return null;
        })
        .toEither()
        .mapLeft(ActivityError::with)
        .map(x -> result);
  }

  /**
   * @param duration of Sleep action
   * @param <U>      return type
   * @return Sleep action
   */
  public static <U> Sleep<U> forA(Duration duration) {
    return new Sleep<>(duration);
  }

  /**
   * optional reason for Sleep action
   *
   * @param reason the reason text
   * @return Sleep action
   */
  public Sleep<T> because(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * constructor: set sleep duration
   *
   * @param duration duration of sleep
   */
  private Sleep(Duration duration) {
    this.duration = duration;
  }
}
