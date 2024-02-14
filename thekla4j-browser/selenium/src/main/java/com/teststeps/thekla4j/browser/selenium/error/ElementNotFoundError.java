package com.teststeps.thekla4j.browser.selenium.error;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.commons.error.ActivityError;

public class ElementNotFoundError extends ActivityError {
    public ElementNotFoundError(String message) {
        super(message);
    }

    public ElementNotFoundError(String message, Throwable cause) {
        super(message, cause);
    }

    public static ElementNotFoundError of(String message, Throwable cause) {
        return new ElementNotFoundError(message, cause);
    }

    public static ElementNotFoundError of(String message) {
        return new ElementNotFoundError(message);
    }


    public static ElementNotFoundError of(Element element) {
        return new ElementNotFoundError("Could not find " + element);
    }
}
