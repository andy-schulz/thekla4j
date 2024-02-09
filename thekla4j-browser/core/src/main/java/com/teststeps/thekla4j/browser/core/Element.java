package com.teststeps.thekla4j.browser.core;

import com.teststeps.thekla4j.browser.core.locator.Locator;
import io.vavr.collection.List;

import java.util.stream.Collectors;

public class Element {

  public final List<Locator> locators;


  public static Element found(Locator locator) {
    return new Element(locator);
  }

  public Element andThen(Locator locator) {
    return new Element(locators.append(locator));
  }

  private Element(List<Locator> locators) {
    this.locators = locators;
  }

  private Element(Locator locator) {
    this.locators = List.of(locator);
  }

  public String toString() {
    return String.format("Element.found(By(%s))", this.locators
        .map(Object::toString)
        .collect(Collectors.joining(" > ")));
  }
}
