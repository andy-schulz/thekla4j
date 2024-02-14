package com.teststeps.thekla4j.browser.core.status;

import java.time.Duration;

public record UntilElement(
    ElementStatusType elementStatusType,
    Duration duration
) {
  public UntilElement forAsLongAs(Duration duration) {
    return new UntilElement(elementStatusType, duration);
  }

  public Duration timeout() {
    return duration;
  }

  public ElementStatusType type() {
    return elementStatusType;
  }

  public static UntilElement of(ElementStatusType type) {
    return new UntilElement(type, Duration.ofSeconds(3));
  }

  public static UntilElement isEnabled() {
    return UntilElement.of(ElementStatusType.IS_ENABLED);
  }

  public static UntilElement isVisible() {
    return UntilElement.of(ElementStatusType.IS_VISIBLE);
  }

  public static UntilElement isClickable() {
    return UntilElement.of(ElementStatusType.IS_CLICKABLE);
  }
}
