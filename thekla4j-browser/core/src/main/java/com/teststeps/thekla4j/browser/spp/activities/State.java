package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.browser.core.Element;
import lombok.With;

@With
public record State(
    Element element,
    Boolean isVisible,
    Boolean isEnabled,
    Boolean isPresent
) {

  public static State of(Element element) {
    return new State(element, false, false, false);
  }
}
