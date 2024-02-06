package com.teststeps.thekla4j.browser.core;

import com.teststeps.thekla4j.browser.core.locator.Locator;
import io.vavr.collection.List;

public class Element {

  public final List<Locator> locators;


  public Element(List<Locator> locators) {
    this.locators = locators;
  }

  public Element(Locator locator) {
    this.locators = List.of(locator);
  }
}
