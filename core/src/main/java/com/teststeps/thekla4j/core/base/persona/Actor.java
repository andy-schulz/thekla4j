package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.activityLog.ActivityLog;
import com.teststeps.thekla4j.activityLog.ActivityStatus;
import com.teststeps.thekla4j.core.activityLog.ProcessLogAnnotation;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.activities.Activity;
import com.teststeps.thekla4j.core.base.errors.DoesNotHave;
import com.teststeps.thekla4j.core.base.errors.DoesNotHaveLogAnnotation;
import com.teststeps.thekla4j.core.base.errors.DoesNotHaveTheAbility;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.HashMap;

public class Actor implements AnswersQuestions, PerformsTask, UsesAbilities {
    private HashMap<String, Ability> abilityMap = new HashMap<String, Ability>();
    private String name = "";
    public ActivityLog activityLog;

    public static Actor named(String name) {
        return new Actor(name);
    }

    public String getName() {
        return this.name;
    }

    public Actor whoCan(Ability ability) {

        this.abilityMap.put(ability.getClass().getName(), ability);

        return this;
    }

    @Override
    public boolean can(Class abilityClass) {
        return this.abilityMap.containsKey(abilityClass.getName());
    }

    @Override
    public <T extends Ability> Ability withAbilityTo(Class<T> abilityClass) throws DoesNotHaveTheAbility {
        if (!this.abilityMap.containsKey(abilityClass.getName())) {
            throw DoesNotHave.theAbility(abilityClass.getSimpleName()).usedBy(this);
        }

        return this.abilityMap.get(abilityClass.getName());
    }

    @Override
    public <PT, RT> RT toAnswer(RT activityResult) {
        return null;
    }


    private <P, R> Either<Throwable, R> perform(Activity<P, R> a, P param) {

        final Try<ProcessLogAnnotation.ActivityLogData> data =
                ProcessLogAnnotation.forActivity(a).andActor(this);

        return data.map(actData -> this.activityLog.addActivityLogEntry(
                a.getClass().getSimpleName(),
                actData.description,
                actData.type,
                ActivityStatus.running))
                .map(logEntry -> logEntry.setInput(param == null ? "" : param.toString()))
                .map(logEntry -> a.performAs(this, param)
                        .peek(r -> {
                            logEntry.status(ActivityStatus.passed);
                            this.activityLog.reset(logEntry);
                        })
                        .peekLeft(l -> {
                            logEntry.status(ActivityStatus.failed);
                            this.activityLog.reset(logEntry);
                        }))
                .recover(DoesNotHaveLogAnnotation.class, x -> a.performAs(this, param))
                .get();

        // create log entry
        // execute activity
        // reset log
        // return activity result
//        return a.performAs(this, param);
    }

    @Override
    public <PT, R1> Either<Throwable, R1> attemptsTo(Activity<PT, R1> a1) {
        return perform(a1, null);
    }

    @Override
    public <PT, R1, R2> Either<Throwable, R2> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2) {
        return this.attemptsTo(a1)
                .flatMap(r -> perform(a2, r));
    }

    @Override
    public <PT, R1, R2, R3> Either<Throwable, R3> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3) {
        return this.attemptsTo(a1, a2)
                .flatMap(r -> perform(a3, r));
    }

    @Override
    public <PT, R1, R2, R3, R4> Either<Throwable, R4> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4) {
        return this.attemptsTo(a1, a2, a3)
                .flatMap(r -> perform(a4, r));
    }

    @Override
    public <PT, R1, R2, R3, R4, R5> Either<Throwable, R5> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5) {
        return this.attemptsTo(a1, a2, a3, a4)
                .flatMap(r -> perform(a5, r));
    }

    @Override
    public <PT, R1, R2, R3, R4, R5, R6> Either<Throwable, R6> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5,
            Activity<R5, R6> a6) {
        return this.attemptsTo(a1, a2, a3, a4, a5)
                .flatMap(r -> perform(a6, r));
    }

    @Override
    public <PT, R1, R2, R3, R4, R5, R6, R7> Either<Throwable, R7> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5,
            Activity<R5, R6> a6,
            Activity<R6, R7> a7) {
        return this.attemptsTo(a1, a2, a3, a4, a5, a6)
                .flatMap(r -> perform(a7, r));
    }

    @Override
    public <PT, R1, R2, R3, R4, R5, R6, R7, R8> Either<Throwable, R8> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5,
            Activity<R5, R6> a6,
            Activity<R6, R7> a7,
            Activity<R7, R8> a8) {
        return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7)
                .flatMap(r -> perform(a8, r));
    }

    @Override
    public <PT, R1, R2, R3, R4, R5, R6, R7, R8, R9> Either<Throwable, R9> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5,
            Activity<R5, R6> a6,
            Activity<R6, R7> a7,
            Activity<R7, R8> a8,
            Activity<R8, R9> a9) {
        return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8)
                .flatMap(r -> perform(a9, r));
    }

    @Override
    public <PT, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> Either<Throwable, R10> attemptsTo(
            Activity<PT, R1> a1,
            Activity<R1, R2> a2,
            Activity<R2, R3> a3,
            Activity<R3, R4> a4,
            Activity<R4, R5> a5,
            Activity<R5, R6> a6,
            Activity<R6, R7> a7,
            Activity<R7, R8> a8,
            Activity<R8, R9> a9,
            Activity<R9, R10> a10) {
        return this.attemptsTo(a1, a2, a3, a4, a5, a6, a7, a8, a9)
                .flatMap(r -> perform(a10, r));
    }

    private Actor(String name) {
        this.name = name;
        this.activityLog = new ActivityLog(name);
    }
}