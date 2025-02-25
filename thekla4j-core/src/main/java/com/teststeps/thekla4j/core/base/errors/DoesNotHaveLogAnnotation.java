package com.teststeps.thekla4j.core.base.errors;

/**
 * Error thrown when a class does not have a log annotation
 */
public class DoesNotHaveLogAnnotation extends StacktraceGenerator {

    /**
     * Create a new DoesNotHaveLogAnnotation
     *
     * @param message - the message of the error
     */
    public DoesNotHaveLogAnnotation(String message) {
        super(message);
    }
}
