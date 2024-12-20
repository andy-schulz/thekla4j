package com.teststeps.thekla4j.browser.core;

import com.teststeps.thekla4j.browser.core.locator.Locator;
import com.teststeps.thekla4j.browser.core.status.UntilElement;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.With;

import java.util.stream.Collectors;

import static com.teststeps.thekla4j.browser.core.status.ElementStatusFunctions.defaultWaiter;

@With
public record Element(
    List<Locator> locators,
    Option<Frame> frame,
    String name,
    Boolean highlight,
    UntilElement waiter) {
  public static Element found(Locator locator) {
    return new Element(
        List.of(locator),
        Option.none(),
        "unnamed",
        true,
        defaultWaiter());
  }

  static Element inFrame(Frame frame, Locator locator) {
    return new Element(
        List.of(locator),
        Option.of(frame),
        "unnamed",
        true,
        defaultWaiter());
  }

  public String toString() {
    return String.format("Element<%s> found By (%s)", name, this.locators
        .map(Object::toString)
        .collect(Collectors.joining(" > ")));
  }

  public Element called(String name) {
    return new Element(locators, frame, name, highlight, waiter);
  }

  public Element andThenFound(Locator locator) {
    return new Element(locators.append(locator), frame, name, highlight, waiter);
  }

  public Element dontHighlight() {
    return new Element(locators, frame, name, false, waiter);
  }

  public Element wait(UntilElement waiter) {
    return new Element(locators, frame, name, highlight, waiter);
  }
}
