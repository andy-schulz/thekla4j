package com.teststeps.thekla4j.browser.selenium.error;

public class SeleniumConfigLoadError extends Throwable {

  public SeleniumConfigLoadError(String errorMessage) {
    super(errorMessage);
  }

  public SeleniumConfigLoadError(String message, Throwable cause) {

    super(message + "\ncaused By:\n" + cause, cause);
  }

  public static SeleniumConfigLoadError with(Throwable ex) {
    return new SeleniumConfigLoadError(ex.getMessage());
  }
  public static SeleniumConfigLoadError with(String message) {
    return new SeleniumConfigLoadError(message);
  }

  public static SeleniumConfigLoadError with(String message, Throwable cause) {
    return new SeleniumConfigLoadError(message, cause);
  }
}
