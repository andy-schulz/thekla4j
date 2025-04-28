package com.teststeps.thekla4j.browser.config;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * The options for the Chrome browser
 * @param binary - the path to the binary
 * @param headless - if the browser should be
 * @param args - the arguments for the browser
 * @param debug - the address of the debugger
 */
public record ChromeOptions(
    @JsonIgnore
    String binary,
    Boolean headless,
    List<String> args,
    DebugOptions debug
) {

  /**
   * get a string representation of the chrome options
   */
  @Override
  public String toString() {
    return "ChromeOptions{" +
      "binary='" + binary + '\'' +
      ", headless=" + headless +
      ", args=" + args +
      ", debugOptions='" + debug + '\'' +
      '}';
  }
}
