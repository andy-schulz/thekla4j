package com.teststeps.thekla4j.browser.selenium.error;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.commons.error.ActivityError;

/**
 * Error thrown when an element could not be found
 */
public class ElementNotFoundError extends ActivityError {

    /**
     * Create a new ElementNotFoundError
     *
     * @param message - the message of the error
     */
    private ElementNotFoundError(String message) {
        super(message);
    }

    /**
     * Create a new ElementNotFoundError
     *
     * @param message - the message of the error
     * @param cause   - the cause of the error
     */
    private ElementNotFoundError(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new ElementNotFoundError
     *
     * @param message - the message of the error
     * @param cause   - the cause of the error
     * @return - a new ElementNotFoundError
     */
    public static ElementNotFoundError of(String message, Throwable cause) {
        return new ElementNotFoundError(message, cause);
    }

    /**
     * Create a new ElementNotFoundError
     *
     * @param message - the message of the error
     * @return - a new ElementNotFoundError
     */
    public static ElementNotFoundError of(String message) {
        return new ElementNotFoundError(message);
    }


    /**
     * Create a new ElementNotFoundError
     *
     * @param element - the element that could not be found
     * @return - a new ElementNotFoundError
     */
    public static ElementNotFoundError of(Element element) {
        return new ElementNotFoundError("Could not find " + element);
    }
}
