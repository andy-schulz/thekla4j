package com.teststeps.thekla4j.core.base.errors;

import com.teststeps.thekla4j.commons.error.ActivityError;

import java.util.Arrays;

public class StacktraceGenerator extends ActivityError {

    public StacktraceGenerator(String message) {
        super(message + "\n\nat " + Arrays.stream(
                Thread.currentThread().getStackTrace())
                .map(StackTraceElement::toString)
        .reduce("", (acc, elem) -> acc + "\n" + elem));
    }
}
