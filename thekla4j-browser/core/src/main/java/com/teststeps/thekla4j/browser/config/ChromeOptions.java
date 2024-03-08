package com.teststeps.thekla4j.browser.config;

import io.vavr.collection.List;

public record ChromeOptions(
    String binary,
    List<String> args,
    String debuggerAddress
) {

  @Override
  public String toString() {
    return "ChromeOptions{" +
      "binary='" + binary + '\'' +
      ", args=" + args +
      ", debuggerAddress='" + debuggerAddress + '\'' +
      '}';
  }
}
