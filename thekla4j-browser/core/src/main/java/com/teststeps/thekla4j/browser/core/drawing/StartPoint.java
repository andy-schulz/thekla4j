package com.teststeps.thekla4j.browser.core.drawing;

public record StartPoint(
  Integer x,
  Integer y
) {
  public static StartPoint on(Integer x, Integer y) {
    return new StartPoint(x, y);
  }
}
