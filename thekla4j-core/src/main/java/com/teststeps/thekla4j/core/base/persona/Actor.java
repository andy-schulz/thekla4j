package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.activityLog.ActivityLogEntry;
import com.teststeps.thekla4j.activityLog.ActivityStatus;
import com.teststeps.thekla4j.activityLog.TheklaActivityLog;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activityLog.ProcessAttachmentAnnotation;
import com.teststeps.thekla4j.core.activityLog.ProcessLogAnnotation;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.activities.Activity;
import com.teststeps.thekla4j.core.base.errors.DoesNotHave;
import com.teststeps.thekla4j.core.base.errors.DoesNotHaveLogAnnotation;
import com.teststeps.thekla4j.core.base.errors.DoesNotHaveTheAbility;
import io.vavr.Function1;
import io.vavr.Function3;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

public class Actor implements PerformsTask, UsesAbilities, HasWorld {
  private final HashMap<String, Ability> abilityMap = new HashMap<>();
  private final String name;
  public final TheklaActivityLog activityLog;

  private ActorsWorld world;

  public static Actor named(String name) {
    return new Actor(name);
  }

  public String getName() {
    return this.name;
  }

  @Override
  public UsesAbilities cleansStage() {

    abilityMap.forEach((name, ability) -> ability.destroy());

    return this;
  }

  public Actor whoCan(Ability... abilities) {

    Arrays.stream(abilities).forEach(ability -> this.abilityMap.put(ability.getClass().getName(), ability));

    return this;
  }

  @Override
  public boolean can(Class<? extends Ability> abilityClass) {
    return this.abilityMap.containsKey(abilityClass.getName());
  }

  @Override
  public <T extends Ability> Ability withAbilityTo(Class<T> abilityClass) throws DoesNotHaveTheAbility {
    if (!this.abilityMap.containsKey(abilityClass.getName())) {
      throw DoesNotHave.theAbility(abilityClass.getSimpleName()).usedBy(this);
    }

    return this.abilityMap.get(abilityClass.getName());
  }

  private final Function3<ActivityStatus, ActivityLogEntry, TheklaActivityLog, Consumer<Option<String>>> setActivityStatus =
      (status, activityLogEntry, actLog) -> output -> {
        activityLogEntry.setOutput(output.getOrElse(""))
            .status(status);
        actLog.reset(activityLogEntry);
      };

  private <P, R> Either<ActivityError, R> perform(Activity<P, R> a, P param) {

    final Try<ProcessLogAnnotation.ActivityLogData> data =
        ProcessLogAnnotation.forActivity(a).withParameter(param).andActor(this);

    return data
        // create an empty activity log
        .map(actData -> this.activityLog
            .addActivityLogEntry(
                a.getClass().getSimpleName(),
                actData.description,
                actData.activityType,
                actData.logType,
                ActivityStatus.running))
        // add the input parameter to the log entry
        .map(logEntry -> logEntry.setInput(param == null ? "" : param.toString()))
        // execute the task
        .map(logEntry -> a.perform(this, param)

            // in case of success set the activity status and add the result parameter to the log entry
            .peek(o -> setActivityStatus.apply(ActivityStatus.passed, logEntry, this.activityLog)
                .accept(Option.of(o).map(Objects.isNull(o) ? x -> "null" : Objects::toString)))

            // in case of failure set the activity status
            .peekLeft(o -> setActivityStatus.apply(ActivityStatus.failed, logEntry, this.activityLog)
                .accept(Option.of(o).map(Objects::toString)))

            // when activity fails set possible attachments (@AttachOnError) to the log entry
            .peekLeft(out -> ProcessAttachmentAnnotation.setAttachment(logEntry, a)))

        // in case the task has no log annotation, just execute the task and return the value
        .recover(DoesNotHaveLogAnnotation.class, x -> a.perform(this, param))
        .get();
  }

  @Override
  public <P, R1> Either<ActivityError, R1> attemptsTo(Activity<P, R1> a1) {
    return perform(a1, null);
  }

  @Override
  public <P, R1, R2> Either<ActivityError, R2> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2
                                                         ) {
    return this.attemptsTo(a1)
        .flatMap(r -> perform(a2, r));
  }

  @Override
  public <P, R1, R2, R3> Either<ActivityError, R3> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3
                                                             ) {
    return this.attemptsTo(a1, a2)
        .flatMap(r -> perform(a3, r));
  }

  @Override
  public <P, R1, R2, R3, R4> Either<ActivityError, R4> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4
                                                                 ) {
    return this.attemptsTo(a1, a2, a3)
        .flatMap(r -> perform(a4, r));
  }

  @Override
  public <P, R1, R2, R3, R4, R5> Either<ActivityError, R5> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5
                                                                     ) {
    return this.attemptsTo(a1, a2, a3, a4)
        .flatMap(r -> perform(a5, r));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6> Either<ActivityError, R6> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6
                                                                         ) {
    return this.attemptsTo(a1, a2, a3, a4, a5)
        .flatMap(r -> perform(a6, r));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7> Either<ActivityError, R7> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7
                                                                             ) {
    return this.attemptsTo(a1, a2, a3, a4, a5, a6)
        .flatMap(r -> perform(a7, r));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8> Either<ActivityError, R8> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8
                                                                                 ) {
    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7)
        .flatMap(r -> perform(a8, r));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> Either<ActivityError, R9> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9
                                                                                     ) {
    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8)
        .flatMap(r -> perform(a9, r));
  }

  @Override
  @SuppressWarnings("java:S119")
  // R10 does not conform to naming convention [A-Z][0-9], its OK here
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Either<ActivityError, R10> attemptsTo(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      Activity<R9, R10> a10
                                                                                           ) {
    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8, a9)
        .flatMap(r -> perform(a10, r));
  }

  @Override
  public <P, R1> Function1<P, Either<ActivityError, R1>> attemptsTo_(
      Activity<P, R1> a1
                                                                    ) {
    return param -> perform(a1, param);
  }

  @Override
  public <P, R1, R2> Function1<P, Either<ActivityError, R2>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2
                                                                        ) {
    return param -> this.attemptsTo_(a1).apply(param)
        .flatMap(r -> perform(a2, r));
  }

  @Override
  public <P, R1, R2, R3> Function1<P, Either<ActivityError, R3>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3
                                                                            ) {
    return param -> this.attemptsTo_(a1, a2).apply(param)
        .flatMap(r -> perform(a3, r));
  }

  @Override
  public <P, R1, R2, R3, R4> Function1<P, Either<ActivityError, R4>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4
                                                                                ) {
    return param -> this.attemptsTo_(a1, a2, a3).apply(param)
        .flatMap(r -> perform(a4, r));
  }

  @Override
  public <P, R1, R2, R3, R4, R5> Function1<P, Either<ActivityError, R5>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5
                                                                                    ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4).apply(param)
        .flatMap(r -> perform(a5, r));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6> Function1<P, Either<ActivityError, R6>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6
                                                                                        ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4, a5).apply(param)
        .flatMap(r -> perform(a6, r));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7> Function1<P, Either<ActivityError, R7>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7
                                                                                            ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4, a5, a6).apply(param)
        .flatMap(r -> perform(a7, r));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8> Function1<P, Either<ActivityError, R8>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8
                                                                                                ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7).apply(param)
        .flatMap(r -> perform(a8, r));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> Function1<P, Either<ActivityError, R9>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9
                                                                                                    ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7, a8).apply(param)
        .flatMap(r -> perform(a9, r));
  }

  @Override
  @SuppressWarnings("java:S119")
  // R10 does not conform to naming convention [A-Z][0-9], its OK here
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Function1<P, Either<ActivityError, R10>> attemptsTo_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      Activity<R9, R10> a10
                                                                                                          ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7, a8, a9).apply(param)
        .flatMap(r -> perform(a10, r));
  }

  @Override
  public <P, R1> Function1<P, Either<ActivityError, R1>> attemptsTo$_(
      Activity<P, R1> a1,
      String group,
      String groupName
                                                                     ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return perform(a1, param)
          .peek(x -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));

    };
  }

  @Override
  public <P, R1, R2> Function1<P, Either<ActivityError, R2>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      String group,
      String groupName
                                                                         ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1).apply(param)
          .flatMap(r -> perform(a2, r))
          .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
    };
  }

  @Override
  public <P, R1, R2, R3> Function1<P, Either<ActivityError, R3>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      String group,
      String groupName
                                                                             ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2).apply(param)
          .flatMap(r -> perform(a3, r))
          .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
    };
  }

  @Override
  public <P, R1, R2, R3, R4> Function1<P, Either<ActivityError, R4>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      String group,
      String groupName
                                                                                 ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3).apply(param)
          .flatMap(r -> perform(a4, r))
          .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
    };
  }

  @Override
  public <P, R1, R2, R3, R4, R5> Function1<P, Either<ActivityError, R5>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      String group,
      String groupName
                                                                                     ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4).apply(param)
          .flatMap(r -> perform(a5, r))
          .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
    };
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6> Function1<P, Either<ActivityError, R6>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      String group,
      String groupName
                                                                                         ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4, a5).apply(param)
          .flatMap(r -> perform(a6, r))
          .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
    };
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7> Function1<P, Either<ActivityError, R7>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      String group,
      String groupName
                                                                                             ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4, a5, a6).apply(param)
          .flatMap(r -> perform(a7, r))
          .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
    };
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8> Function1<P, Either<ActivityError, R8>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      String group,
      String groupName
                                                                                                 ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7).apply(param)
          .flatMap(r -> perform(a8, r))
          .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
    };
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> Function1<P, Either<ActivityError, R9>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      String group,
      String groupName
                                                                                                     ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7, a8).apply(param)
          .flatMap(r -> perform(a9, r))
          .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
    };
  }

  @Override
  @SuppressWarnings("java:S119")
  // R10 does not conform to naming convention [A-Z][0-9], its OK here
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Function1<P, Either<ActivityError, R10>> attemptsTo$_(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      Activity<R9, R10> a10,
      String group,
      String groupName
                                                                                                           ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7, a8, a9).apply(param)
          .flatMap(r -> perform(a10, r))
          .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
    };
  }

  @Override
  public <P, R1> Either<ActivityError, R1> attemptsTo$(
      Activity<P, R1> a1,
      String group,
      String groupName
                                                      ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return perform(a1, null)
        .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));

  }

  @Override
  public <P, R1, R2> Either<ActivityError, R2> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      String group,
      String groupName
                                                          ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1)
        .flatMap(r -> perform(a2, r))
        .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
  }

  @Override
  public <P, R1, R2, R3> Either<ActivityError, R3> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      String group,
      String groupName
                                                              ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2)
        .flatMap(r -> perform(a3, r))
        .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
  }

  @Override
  public <P, R1, R2, R3, R4> Either<ActivityError, R4> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      String group,
      String groupName
                                                                  ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3)
        .flatMap(r -> perform(a4, r))
        .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
  }

  @Override
  public <P, R1, R2, R3, R4, R5> Either<ActivityError, R5> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      String group,
      String groupName
                                                                      ) {

    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4)
        .flatMap(r -> perform(a5, r))
        .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6> Either<ActivityError, R6> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      String group,
      String groupName
                                                                          ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4, a5)
        .flatMap(r -> perform(a6, r))
        .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7> Either<ActivityError, R7> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      String group,
      String groupName
                                                                              ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4, a5, a6)
        .flatMap(r -> perform(a7, r))
        .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8> Either<ActivityError, R8> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      String group,
      String groupName
                                                                                  ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7)
        .flatMap(r -> perform(a8, r))
        .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
  }

  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> Either<ActivityError, R9> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      String group,
      String groupName
                                                                                      ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8)
        .flatMap(r -> perform(a9, r))
        .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
  }

  @Override
  @SuppressWarnings("java:S119")
  // R10 does not conform to naming convention [A-Z][0-9], its OK here
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Either<ActivityError, R10> attemptsTo$(
      Activity<P, R1> a1,
      Activity<R1, R2> a2,
      Activity<R2, R3> a3,
      Activity<R3, R4> a4,
      Activity<R4, R5> a5,
      Activity<R5, R6> a6,
      Activity<R6, R7> a7,
      Activity<R7, R8> a8,
      Activity<R8, R9> a9,
      Activity<R9, R10> a10,
      String group,
      String groupName
                                                                                            ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8, a9)
        .flatMap(r -> perform(a10, r))
        .peek(res -> this.activityLog.reset(entry), x -> this.activityLog.reset(entry));
  }


  private Actor(String name) {
    this.name = name;
    this.activityLog = new TheklaActivityLog(name);
  }

  @Override
  public ActorsWorld getWorld() {
    return world;
  }

  @Override
  public Actor withWorld(ActorsWorld world) {
    this.world = world;
    return this;
  }
}