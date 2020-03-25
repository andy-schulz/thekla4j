package com.teststeps.thekla4j.core.base.errors;

public class DetectedNullObject extends StacktraceGenerator {

    public static DetectedNullObject forProperty(String property) {
        return new DetectedNullObject("Property  " + property + " is null, but should not.");
    }

    private DetectedNullObject(String message) {
        super(message);
    }
}
