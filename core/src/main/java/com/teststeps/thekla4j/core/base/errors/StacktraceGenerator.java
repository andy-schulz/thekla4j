package com.teststeps.thekla4j.core.base.errors;

import java.util.Arrays;

public class StacktraceGenerator extends Exception {

    public StacktraceGenerator(String message) {
        super(message + "\n\nat " + Arrays.stream(
                Thread.currentThread().getStackTrace())
                .map(StackTraceElement::toString)
        .reduce("", (acc, elem) -> acc + "\n" + elem));
    }
}
