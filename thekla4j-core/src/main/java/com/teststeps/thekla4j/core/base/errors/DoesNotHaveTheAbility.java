package com.teststeps.thekla4j.core.base.errors;

/**
 * Error thrown when an actor does not have a certain ability
 */
public class DoesNotHaveTheAbility extends StacktraceGenerator {

    /**
     * Create a new DoesNotHaveTheAbility
     *
     * @param message - the message of the error
     */
    public DoesNotHaveTheAbility(String message) {
        super(message);
    }
}