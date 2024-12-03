package com.teststeps.thekla4j.browser.core;


import com.teststeps.thekla4j.browser.core.locator.Locator;
import io.vavr.collection.List;
import lombok.With;

import java.time.Duration;


@With
public record Frame(
  List<Locator> locators,
  Duration timeout
) {

  public static Frame found(Locator locator) {
    return new Frame(List.of(locator), Duration.ofSeconds(5));
  }

  public Element elementFound(Locator locator) {
    return Element.inFrame(this, locator);
  }

  private String locatorString(Frame frame) {
    return frame.locators
        .map(Locator::locatorString)
        .mkString(" > ");
  }

  public boolean equals(Frame frame) {
    return locatorString(this).equals(locatorString(frame));
  }
}
