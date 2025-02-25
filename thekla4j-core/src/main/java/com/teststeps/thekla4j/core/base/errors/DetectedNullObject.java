package com.teststeps.thekla4j.core.base.errors;

/**
 * Error thrown when a property of a task is null
 */
public class DetectedNullObject extends StacktraceGenerator {

    /**
     * Create a new DetectedNullObject
     *
     * @param property - the property that is null
     * @return - a new DetectedNullObject
     */
    public static DetectedNullObject forProperty(String property) {
        return new DetectedNullObject("Property  " + property + " is null, but should not.");
    }

    private DetectedNullObject(String message) {
        super(message);
    }
}
