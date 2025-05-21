package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.activityLog.ActivityLogEntry;
import com.teststeps.thekla4j.activityLog.ActivityStatus;
import com.teststeps.thekla4j.activityLog.TheklaActivityLog;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activityLog.ProcessAttachmentAnnotation;
import com.teststeps.thekla4j.core.activityLog.ProcessLogAnnotation;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.errors.DoesNotHave;
import com.teststeps.thekla4j.core.base.errors.DoesNotHaveLogAnnotation;
import com.teststeps.thekla4j.core.base.errors.DoesNotHaveTheAbility;
import io.vavr.Function3;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.Getter;

/**
 * The actor is the person who performs the tasks
 */
public class Actor implements PerformsTask, UsesAbilities, HasWorld {
  private final HashMap<String, Ability> abilityMap = new HashMap<>();

  /**
   * the name of the actor
   * 
   * @return the name of the actor
   */
  @Getter
  private final String name;

  /**
   * the actors activity log
   */
  public final TheklaActivityLog activityLog;

  private ActorsWorld world;

  /**
   * Create a new actor with the given name
   *
   * @param name the name of the actor
   * @return the actor
   */
  public static Actor named(String name) {
    return new Actor(name);
  }

  /**
   * create and attach the ability dump of all abilities to the log
   * 
   * @return the actor
   */
  public Actor attachAbilityDumpToLog() {

    List.ofAll(this.abilityMap.values())
        .flatMap(Ability::abilityLogDump)
        .forEach(this.activityLog::appendAttachmentsToRootNode);

    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UsesAbilities cleansStage() {

    abilityMap.forEach((name, ability) -> ability.destroy());

    return this;
  }

  /**
   * assign new abilities to the actor
   *
   * @param abilities list of abilities
   * @return the actor
   */
  public Actor whoCan(Ability... abilities) {

    Arrays.stream(abilities).forEach(ability -> this.abilityMap.put(ability.getClass().getName(), ability));

    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean can(Class<? extends Ability> abilityClass) {
    return this.abilityMap.containsKey(abilityClass.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends Ability> Ability withAbilityTo(Class<T> abilityClass) throws DoesNotHaveTheAbility {
    if (!this.abilityMap.containsKey(abilityClass.getName())) {
      throw DoesNotHave.theAbility(abilityClass.getSimpleName()).usedBy(this);
    }

    return this.abilityMap.get(abilityClass.getName());
  }

  private final Function3<ActivityStatus, ActivityLogEntry, TheklaActivityLog, Consumer<Option<String>>> setActivityStatus =
      (status, activityLogEntry, actLog) -> output -> {
        activityLogEntry
            .setOutput(output.getOrElse(""))
            .setEndTime(LocalDateTime.now())
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
                .accept(Option.of(o).map(Objects::toString)))

            // in case of failure set the activity status
            .peekLeft(o -> setActivityStatus.apply(ActivityStatus.failed, logEntry, this.activityLog)
                .accept(Option.of(o).map(Objects::toString)))

            // when activity fails set possible attachments (@AttachOnError) to the log entry
            .peekLeft(out -> ProcessAttachmentAnnotation.setAttachment(logEntry, a)))

        // in case the task has no log annotation, just execute the task and return the value
        .recover(DoesNotHaveLogAnnotation.class, x -> a.perform(this, param))
        .get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1> Either<ActivityError, R1> attemptsTo(Activity<Void, R1> a1) {
    return perform(a1, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2> Either<ActivityError, R2> attemptsTo(
                                                       Activity<Void, R1> a1, Activity<R1, R2> a2
  ) {
    return this.attemptsTo(a1)
        .flatMap(r -> perform(a2, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3> Either<ActivityError, R3> attemptsTo(
                                                           Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3
  ) {
    return this.attemptsTo(a1, a2)
        .flatMap(r -> perform(a3, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4> Either<ActivityError, R4> attemptsTo(
                                                               Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4
  ) {
    return this.attemptsTo(a1, a2, a3)
        .flatMap(r -> perform(a4, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5> Either<ActivityError, R5> attemptsTo(
                                                                   Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5
  ) {
    return this.attemptsTo(a1, a2, a3, a4)
        .flatMap(r -> perform(a5, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6> Either<ActivityError, R6> attemptsTo(
                                                                       Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6
  ) {
    return this.attemptsTo(a1, a2, a3, a4, a5)
        .flatMap(r -> perform(a6, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7> Either<ActivityError, R7> attemptsTo(
                                                                           Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7
  ) {
    return this.attemptsTo(a1, a2, a3, a4, a5, a6)
        .flatMap(r -> perform(a7, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7, R8> Either<ActivityError, R8> attemptsTo(
                                                                               Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8
  ) {
    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7)
        .flatMap(r -> perform(a8, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7, R8, R9> Either<ActivityError, R9> attemptsTo(
                                                                                   Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9
  ) {
    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8)
        .flatMap(r -> perform(a9, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("java:S119")
  // R10 does not conform to naming convention [A-Z][0-9], its OK here
  public <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Either<ActivityError, R10> attemptsTo(
                                                                                         Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, Activity<R9, R10> a10
  ) {
    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8, a9)
        .flatMap(r -> perform(a10, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1> AttemptsWith<P, Either<ActivityError, R1>> attemptsTo_(
                                                                        Activity<P, R1> a1
  ) {
    return param -> perform(a1, param);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2> AttemptsWith<P, Either<ActivityError, R2>> attemptsTo_(
                                                                            Activity<P, R1> a1, Activity<R1, R2> a2
  ) {
    return param -> this.attemptsTo_(a1)
        .using(param)
        .flatMap(r -> perform(a2, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3> AttemptsWith<P, Either<ActivityError, R3>> attemptsTo_(
                                                                                Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3
  ) {
    return param -> this.attemptsTo_(a1, a2)
        .using(param)
        .flatMap(r -> perform(a3, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4> AttemptsWith<P, Either<ActivityError, R4>> attemptsTo_(
                                                                                    Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4
  ) {
    return param -> this.attemptsTo_(a1, a2, a3)
        .using(param)
        .flatMap(r -> perform(a4, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5> AttemptsWith<P, Either<ActivityError, R5>> attemptsTo_(
                                                                                        Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5
  ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4)
        .using(param)
        .flatMap(r -> perform(a5, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6> AttemptsWith<P, Either<ActivityError, R6>> attemptsTo_(
                                                                                            Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6
  ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4, a5)
        .using(param)
        .flatMap(r -> perform(a6, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7> AttemptsWith<P, Either<ActivityError, R7>> attemptsTo_(
                                                                                                Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7
  ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4, a5, a6)
        .using(param)
        .flatMap(r -> perform(a7, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8> AttemptsWith<P, Either<ActivityError, R8>> attemptsTo_(
                                                                                                    Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8
  ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7)
        .using(param)
        .flatMap(r -> perform(a8, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> AttemptsWith<P, Either<ActivityError, R9>> attemptsTo_(
                                                                                                        Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9
  ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7, a8)
        .using(param)
        .flatMap(r -> perform(a9, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("java:S119")
  // R10 does not conform to naming convention [A-Z][0-9], its OK here
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> AttemptsWith<P, Either<ActivityError, R10>> attemptsTo_(
                                                                                                              Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, Activity<R9, R10> a10
  ) {
    return param -> this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7, a8, a9)
        .using(param)
        .flatMap(r -> perform(a10, r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1> AttemptsWith<P, Either<ActivityError, R1>> attemptsTo$_(
                                                                         Activity<P, R1> a1, String group, String groupName
  ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return perform(a1, param)
          .peek(x -> this.activityLog.reset(entry))
          .peekLeft(x -> this.activityLog.reset(entry));

    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2> AttemptsWith<P, Either<ActivityError, R2>> attemptsTo$_(
                                                                             Activity<P, R1> a1, Activity<R1, R2> a2, String group, String groupName
  ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1)
          .using(param)
          .flatMap(r -> perform(a2, r))
          .peek(res -> this.activityLog.reset(entry))
          .peekLeft(x -> this.activityLog.reset(entry));
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3> AttemptsWith<P, Either<ActivityError, R3>> attemptsTo$_(
                                                                                 Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, String group, String groupName
  ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2)
          .using(param)
          .flatMap(r -> perform(a3, r))
          .peek(res -> this.activityLog.reset(entry))
          .peekLeft(x -> this.activityLog.reset(entry));
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4> AttemptsWith<P, Either<ActivityError, R4>> attemptsTo$_(
                                                                                     Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, String group, String groupName
  ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3)
          .using(param)
          .flatMap(r -> perform(a4, r))
          .peek(res -> this.activityLog.reset(entry))
          .peekLeft(x -> this.activityLog.reset(entry));
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5> AttemptsWith<P, Either<ActivityError, R5>> attemptsTo$_(
                                                                                         Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, String group, String groupName
  ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4)
          .using(param)
          .flatMap(r -> perform(a5, r))
          .peek(res -> this.activityLog.reset(entry))
          .peekLeft(x -> this.activityLog.reset(entry));
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6> AttemptsWith<P, Either<ActivityError, R6>> attemptsTo$_(
                                                                                             Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, String group, String groupName
  ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4, a5)
          .using(param)
          .flatMap(r -> perform(a6, r))
          .peek(res -> this.activityLog.reset(entry))
          .peekLeft(x -> this.activityLog.reset(entry));
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7> AttemptsWith<P, Either<ActivityError, R7>> attemptsTo$_(
                                                                                                 Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, String group, String groupName
  ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4, a5, a6)
          .using(param)
          .flatMap(r -> perform(a7, r))
          .peek(res -> this.activityLog.reset(entry))
          .peekLeft(x -> this.activityLog.reset(entry));
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8> AttemptsWith<P, Either<ActivityError, R8>> attemptsTo$_(
                                                                                                     Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, String group, String groupName
  ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7)
          .using(param)
          .flatMap(r -> perform(a8, r))
          .peek(res -> this.activityLog.reset(entry))
          .peekLeft(x -> this.activityLog.reset(entry));
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9> AttemptsWith<P, Either<ActivityError, R9>> attemptsTo$_(
                                                                                                         Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, String group, String groupName
  ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7, a8)
          .using(param)
          .flatMap(r -> perform(a9, r))
          .peek(res -> this.activityLog.reset(entry))
          .peekLeft(x -> this.activityLog.reset(entry));
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("java:S119")
  // R10 does not conform to naming convention [A-Z][0-9], its OK here
  public <P, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> AttemptsWith<P, Either<ActivityError, R10>> attemptsTo$_(
                                                                                                               Activity<P, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, Activity<R9, R10> a10, String group, String groupName
  ) {
    return (param) -> {

      ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

      return this.attemptsTo_(a1, a2, a3, a4, a5, a6, a7, a8, a9)
          .using(param)
          .flatMap(r -> perform(a10, r))
          .peek(res -> this.activityLog.reset(entry))
          .peekLeft(x -> this.activityLog.reset(entry));
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1> Either<ActivityError, R1> attemptsTo$(
                                                    Activity<Void, R1> a1, String group, String groupName
  ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return perform(a1, null)
        .peek(res -> this.activityLog.reset(entry))
        .peekLeft(x -> this.activityLog.reset(entry));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2> Either<ActivityError, R2> attemptsTo$(
                                                        Activity<Void, R1> a1, Activity<R1, R2> a2, String group, String groupName
  ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1)
        .flatMap(r -> perform(a2, r))
        .peek(res -> this.activityLog.reset(entry))
        .peekLeft(x -> this.activityLog.reset(entry));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3> Either<ActivityError, R3> attemptsTo$(
                                                            Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, String group, String groupName
  ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2)
        .flatMap(r -> perform(a3, r))
        .peek(res -> this.activityLog.reset(entry))
        .peekLeft(x -> this.activityLog.reset(entry));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4> Either<ActivityError, R4> attemptsTo$(
                                                                Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, String group, String groupName
  ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3)
        .flatMap(r -> perform(a4, r))
        .peek(res -> this.activityLog.reset(entry))
        .peekLeft(x -> this.activityLog.reset(entry));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5> Either<ActivityError, R5> attemptsTo$(
                                                                    Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, String group, String groupName
  ) {

    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4)
        .flatMap(r -> perform(a5, r))
        .peek(res -> this.activityLog.reset(entry))
        .peekLeft(x -> this.activityLog.reset(entry));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6> Either<ActivityError, R6> attemptsTo$(
                                                                        Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, String group, String groupName
  ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4, a5)
        .flatMap(r -> perform(a6, r))
        .peek(res -> this.activityLog.reset(entry))
        .peekLeft(x -> this.activityLog.reset(entry));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7> Either<ActivityError, R7> attemptsTo$(
                                                                            Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, String group, String groupName
  ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4, a5, a6)
        .flatMap(r -> perform(a7, r))
        .peek(res -> this.activityLog.reset(entry))
        .peekLeft(x -> this.activityLog.reset(entry));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7, R8> Either<ActivityError, R8> attemptsTo$(
                                                                                Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, String group, String groupName
  ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7)
        .flatMap(r -> perform(a8, r))
        .peek(res -> this.activityLog.reset(entry))
        .peekLeft(x -> this.activityLog.reset(entry));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <R1, R2, R3, R4, R5, R6, R7, R8, R9> Either<ActivityError, R9> attemptsTo$(
                                                                                    Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, String group, String groupName
  ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8)
        .flatMap(r -> perform(a9, r))
        .peek(res -> this.activityLog.reset(entry))
        .peekLeft(x -> this.activityLog.reset(entry));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("java:S119")
  // R10 does not conform to naming convention [A-Z][0-9], its OK here
  public <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Either<ActivityError, R10> attemptsTo$(
                                                                                          Activity<Void, R1> a1, Activity<R1, R2> a2, Activity<R2, R3> a3, Activity<R3, R4> a4, Activity<R4, R5> a5, Activity<R5, R6> a6, Activity<R6, R7> a7, Activity<R7, R8> a8, Activity<R8, R9> a9, Activity<R9, R10> a10, String group, String groupName
  ) {
    ActivityLogEntry entry = this.activityLog.addGroup(group, groupName);

    return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8, a9)
        .flatMap(r -> perform(a10, r))
        .peek(res -> this.activityLog.reset(entry))
        .peekLeft(x -> this.activityLog.reset(entry));
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