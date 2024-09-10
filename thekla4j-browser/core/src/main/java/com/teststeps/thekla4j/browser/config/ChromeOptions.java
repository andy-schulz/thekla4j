package com.teststeps.thekla4j.browser.config;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public record ChromeOptions(
    @JsonIgnore
    String binary,
    Boolean headless,
    List<String> args,
    String debuggerAddress
) {

  @Override
  public String toString() {
    return "ChromeOptions{" +
      "binary='" + binary + '\'' +
      ", headless=" + headless +
      ", args=" + args +
      ", debuggerAddress='" + debuggerAddress + '\'' +
      '}';
  }
}
