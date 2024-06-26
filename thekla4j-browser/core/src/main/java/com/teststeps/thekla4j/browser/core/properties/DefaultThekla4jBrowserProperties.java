package com.teststeps.thekla4j.browser.core.properties;

import com.teststeps.thekla4j.commons.properties.PropertyElement;

public enum DefaultThekla4jBrowserProperties {

  HIGHLIGHT_ELEMENTS(PropertyElement.of("thekla4j.browser.highlightElements", "true")),
  SLOW_DOWN_EXECUTION(PropertyElement.of("thekla4j.browser.slowDownExecution", "false")),
  SLOW_DOWN_TIME(PropertyElement.of("thekla4j.browser.slowDownTimeInSeconds", "1")),

  SCREENSHOT_RELATIVE_PATH(PropertyElement.of("thekla4j.browser.screenshot.relativePath", "")),

  SCREENSHOT_ABSOLUTE_PATH(PropertyElement.of("thekla4j.browser.screenshot.absolutePath", System.getProperty("user.dir")));

  final PropertyElement property;

  public PropertyElement property() {
    return property;
  }

  DefaultThekla4jBrowserProperties(PropertyElement property) {
    this.property = property;
  }
}
