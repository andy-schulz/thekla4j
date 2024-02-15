package com.teststeps.thekla4j.browser.core;

public record Cookie(
    String name,
    String value) {

  public static Cookie of(String name, String value) {
    return new Cookie(name, value);
  }
}
