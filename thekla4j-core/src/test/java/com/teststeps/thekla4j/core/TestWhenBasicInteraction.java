package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

public class TestWhenBasicInteraction {

  @Test
  void when_true_executesTheTask() {
    final Actor actor = Actor.named("TestActor");
    final List<String> log = new ArrayList<>();

    actor.attemptsTo(WhenTrackingInteraction.track(log).when(true));

    assertThat("task was executed", log.size(), equalTo(1));
  }

  @Test
  void when_false_skipsTheTask() {
    final Actor actor = Actor.named("TestActor");
    final List<String> log = new ArrayList<>();

    actor.attemptsTo(WhenTrackingInteraction.track(log).when(false));

    assertThat("task was NOT executed", log.size(), equalTo(0));
  }

  @Test
  void when_false_returnsRightNotAnError() {
    final Actor actor = Actor.named("TestActor");

    final Either<ActivityError, Void> result =
        actor.attemptsTo(WhenTrackingInteraction.track(new ArrayList<>()).when(false));

    assertThat("skipped task returns Either.right (no error)", result.isRight(), equalTo(true));
  }

  @Test
  void when_true_returnsRight() {
    final Actor actor = Actor.named("TestActor");

    final Either<ActivityError, Void> result =
        actor.attemptsTo(WhenTrackingInteraction.track(new ArrayList<>()).when(true));

    assertThat("executed task returns Either.right", result.isRight(), equalTo(true));
  }

  @Test
  void noCondition_alwaysExecutes() {
    final Actor actor = Actor.named("TestActor");
    final List<String> log = new ArrayList<>();

    actor.attemptsTo(WhenTrackingInteraction.track(log));

    assertThat("task without condition is always executed", log.size(), equalTo(1));
  }

  @Test
  void when_supplier_isEvaluatedLazily() {
    final Actor actor = Actor.named("TestActor");
    final List<String> log = new ArrayList<>();
    final AtomicBoolean flag = new AtomicBoolean(false);

    final BasicInteraction task = WhenTrackingInteraction.track(log).when(flag::get);

    actor.attemptsTo(task);
    assertThat("task skipped while flag=false", log.size(), equalTo(0));

    flag.set(true);
    actor.attemptsTo(task);
    assertThat("task executed after flag=true", log.size(), equalTo(1));
  }

  @Test
  void when_supplier_canToggleMultipleTimes() {
    final Actor actor = Actor.named("TestActor");
    final List<String> log = new ArrayList<>();
    final AtomicBoolean flag = new AtomicBoolean(true);

    final BasicInteraction task = WhenTrackingInteraction.track(log).when(flag::get);

    actor.attemptsTo(task);
    assertThat("executed on first run (flag=true)", log.size(), equalTo(1));

    flag.set(false);
    actor.attemptsTo(task);
    assertThat("skipped on second run (flag=false)", log.size(), equalTo(1));

    flag.set(true);
    actor.attemptsTo(task);
    assertThat("executed again on third run (flag=true)", log.size(), equalTo(2));
  }

  @Test
  void when_preservesClassName_true() {
    final List<String> log = new ArrayList<>();
    final BasicInteraction task = WhenTrackingInteraction.track(log).when(true);

    assertThat("class name preserved after when(true)", task.toString(), equalTo("WhenTrackingInteraction"));
  }

  @Test
  void when_preservesClassName_false() {
    final List<String> log = new ArrayList<>();
    final BasicInteraction task = WhenTrackingInteraction.track(log).when(false);

    assertThat("class name preserved after when(false)", task.toString(), equalTo("WhenTrackingInteraction"));
  }
}

class WhenTrackingInteraction extends BasicInteraction {

  private final List<String> log;

  private WhenTrackingInteraction(final List<String> log) {
    this.log = log;
  }

  @Override
  protected Either<ActivityError, Void> performAs(final Actor actor) {
    log.add("executed");
    return Either.right(null);
  }

  static WhenTrackingInteraction track(final List<String> log) {
    return new WhenTrackingInteraction(log);
  }
}
