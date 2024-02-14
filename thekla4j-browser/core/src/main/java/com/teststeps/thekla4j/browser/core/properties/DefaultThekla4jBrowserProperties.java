package com.teststeps.thekla4j.browser.core.properties;

import com.teststeps.thekla4j.commons.properties.PropertyElement;

public enum DefaultThekla4jBrowserProperties {

  HIGHLIGHT_ELEMENTS(PropertyElement.of("thekla4j.highlightElements", "true")),
  SLOW_DOWN_EXECUTION(PropertyElement.of("thekla4j.slowDownExecution", "false")),
  SLOW_DOWN_TIME(PropertyElement.of("thekla4j.slowDownTimeInSeconds", "1"));


  final PropertyElement property;

  public PropertyElement property() {
    return property;
  }

  DefaultThekla4jBrowserProperties(PropertyElement property) {
    this.property = property;
  }
}
