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
    UntilElement waiter) {
  public static Element found(Locator locator) {
    return new Element(
        List.of(locator),
        defaultWaiter());
  }

  public String toString() {
    return String.format("Element.found(By(%s))", this.locators
        .map(Object::toString)
        .collect(Collectors.joining(" > ")));
  }

  public Element wait(UntilElement waiter) {
    return new Element(locators, waiter);
  }
}
