package com.teststeps.thekla4j.core.activityLog;

import com.teststeps.thekla4j.activityLog.ActivityLogEntryType;
import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.core.base.activities.Activity;
import com.teststeps.thekla4j.core.base.errors.DetectedNullObject;
import com.teststeps.thekla4j.core.base.errors.DoesNotHave;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Try;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessLogAnnotation {

    public static class ProcessAnnotationHelper {
        private Activity activity;

        ProcessAnnotationHelper(Activity activity) {
            this.activity = activity;
        }

        public Try<ActivityLogData>
        andActor(Actor actor) {
            return new ProcessLogAnnotation(this.activity, actor).process();
        }
    }

    public static class ActivityLogData {
        public String description;
        public ActivityLogEntryType type;

        public ActivityLogData(String description, ActivityLogEntryType type) {
            this.description = description;
            this.type = type;
        }
    }

    private Activity activity;
    private Actor actor;

    public static ProcessAnnotationHelper forActivity(Activity activity) {
        return new ProcessAnnotationHelper(activity);
    }

    private Try<ActivityLogData> process() {
        if (this.activity == null)
            return Try.failure(DetectedNullObject.forProperty(this.getClass().getSimpleName() + "::activity"));

        if (this.actor == null)
            return Try.failure(DetectedNullObject.forProperty(this.getClass().getSimpleName() + "::actor"));

        Class<?> clazz = this.activity.getClass();

        final Workflow flowAnT = clazz.getAnnotation(Workflow.class);
        final Action interactionAnT = clazz.getAnnotation(Action.class);

        if (flowAnT == null && interactionAnT == null)
            return Try.failure(DoesNotHave
                    .logAnnotation(Workflow.class.getSimpleName() + " or " + Action.class.getSimpleName())
                    .in(this.activity));


        return Try.success(this.processCalledFields(
                flowAnT != null ?
                        flowAnT.value() :
                        interactionAnT.value(),
                clazz,
                flowAnT != null ?
                        ActivityLogEntryType.Task :
                        ActivityLogEntryType.Interaction));
    }

    private ActivityLogData processCalledFields(String flowValue, Class<?> clazz, ActivityLogEntryType t) {

        final HashMap<String, String> calledAnnotation = new HashMap<String, String>();

        // get all locally declared fields and all inherited public fields and check for annotations
        Stream.of(Arrays.asList(clazz.getFields()), Arrays.asList(clazz.getDeclaredFields()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList()).forEach(field -> {

            final Called calledAnT = field.getAnnotation(Called.class);

            if (calledAnT == null)
                return;

            try {
                if (Modifier.isPrivate(field.getModifiers()))
                    field.setAccessible(true);

                calledAnnotation.put("@{" + calledAnT.value() + "}", field.get(activity).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        final String description = calledAnnotation.entrySet()
                .stream()
                .reduce(flowValue,
                        (acc, entry) -> acc.replace(entry.getKey(), entry.getValue()),
                        (comb1, comb2) -> null);

        return new ActivityLogData(description, t);
    }

    ProcessLogAnnotation(Activity activity, Actor actor) {
        this.activity = activity;
        this.actor = actor;
    }
}
