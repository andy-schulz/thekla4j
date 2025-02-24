package com.teststeps.thekla4j.browser.selenium.error;

/**
 * Error when loading the Selenium configuration
 */
public class SeleniumConfigLoadError extends Throwable {

  private SeleniumConfigLoadError(String errorMessage) {
    super(errorMessage);
  }

  private SeleniumConfigLoadError(String message, Throwable cause) {

    super(message + "\ncaused By:\n" + cause, cause);
  }

  /**
   * Create a new SeleniumConfigLoadError
   *
   * @param ex - the exception
   * @return - the SeleniumConfigLoadError
   */
  public static SeleniumConfigLoadError with(Throwable ex) {
    return new SeleniumConfigLoadError(ex.getMessage());
  }

  /**
   * Create a new SeleniumConfigLoadError
   *
   * @param message - the message
   * @return - the SeleniumConfigLoadError
   */
  public static SeleniumConfigLoadError with(String message) {
    return new SeleniumConfigLoadError(message);
  }

  /**
   * Create a new SeleniumConfigLoadError
   *
   * @param message - the message
   * @param cause - the cause
   * @return - the SeleniumConfigLoadError
   */
  public static SeleniumConfigLoadError with(String message, Throwable cause) {
    return new SeleniumConfigLoadError(message, cause);
  }
}
