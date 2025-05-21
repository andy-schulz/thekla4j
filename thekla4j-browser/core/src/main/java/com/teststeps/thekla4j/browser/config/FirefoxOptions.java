package com.teststeps.thekla4j.browser.config;

import java.util.List;

/**
 * The options for the Firefox browser
 * 
 * @param args - the arguments for the browser
 */
public record FirefoxOptions(
                             List<String> args) {

  /**
   * get a string representation of the firefox options
   */
  @Override
  public String toString() {
    return "FirefoxOptions{" +
        "args=" + args +
        '}';
  }

  public static String help() {
    return """
        args: [] # browser arguments - Example: ["--headless", "--disable-gpu"]
        """;
  }
}
