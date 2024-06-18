package com.teststeps.thekla4j.browser.core;

import com.teststeps.thekla4j.browser.core.locator.Locator;
import com.teststeps.thekla4j.browser.core.status.UntilElement;
import io.vavr.collection.List;
import lombok.With;

import java.util.stream.Collectors;

import static com.teststeps.thekla4j.browser.core.status.ElementStatusFunctions.defaultWaiter;

@With
public record Element(
    List<Locator> locators,
    String name,
    UntilElement waiter) {
  public static Element found(Locator locator) {
    return new Element(
        List.of(locator),
        "unnamed",
        defaultWaiter());
  }

  public String toString() {
    return String.format("Element<%s> found By (%s)", name, this.locators
        .map(Object::toString)
        .collect(Collectors.joining(" > ")));
  }

  public Element called(String name) {
    return new Element(locators, name, waiter);
  }

  public Element andThenFound(Locator locator) {
    return new Element(locators.append(locator), name, waiter);
  }

  public Element wait(UntilElement waiter) {
    return new Element(locators, name, waiter);
  }
}
