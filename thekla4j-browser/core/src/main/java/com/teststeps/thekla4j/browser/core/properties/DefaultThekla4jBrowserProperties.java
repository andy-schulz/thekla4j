package com.teststeps.thekla4j.browser.core.properties;

import com.teststeps.thekla4j.commons.properties.PropertyElement;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.collection.List;


public enum DefaultThekla4jBrowserProperties {

  HIGHLIGHT_ELEMENTS(PropertyElement.of("thekla4j.browser.highlightElements", "true", "Possible values: true, false")),
  SLOW_DOWN_EXECUTION(PropertyElement.of("thekla4j.browser.slowDownExecution", "false", "Possible values: true, false")),
  SLOW_DOWN_TIME(PropertyElement.of("thekla4j.browser.slowDownTimeInSeconds", "1", "Time in seconds to slow down the execution")),

  AUTO_SCROLL_ENABLED(PropertyElement.of("thekla4j.browser.autoScroll.enabled", "false", "Possible values: true, false")),
  AUTO_SCROLL_VERTICAL(PropertyElement.of("thekla4j.browser.autoScroll.vertical", "center", "Possible values: top, center, bottom")),

  SCREENSHOT_RELATIVE_PATH(PropertyElement.of("thekla4j.browser.screenshot.relativePath", "", "Relative project path to store the screenshots")),
  SCREENSHOT_ABSOLUTE_PATH(PropertyElement.of("thekla4j.browser.screenshot.absolutePath", System.getProperty("user.dir"), "Absolute path to store the screenshots")),;

  final PropertyElement property;

  public PropertyElement property() {
    return property;
  }

  public String value() {
    return Thekla4jProperty.of(property);
  }

  public static String help() {
    // iterate over all enums and return the help text
    return List.of(DefaultThekla4jBrowserProperties.values())
        .map(DefaultThekla4jBrowserProperties::property)
        .map(property -> property.name() + ": " + property.helpText() + " (default: " + property.defaultValue() + ")")
        .mkString("\n");
  }

  DefaultThekla4jBrowserProperties(PropertyElement property) {
    this.property = property;
  }
}
