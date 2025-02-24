package com.teststeps.thekla4j.browser.core.properties;

import com.teststeps.thekla4j.commons.properties.PropertyElement;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.collection.List;


/**
 * Default properties for thekla4j browser
 */
public enum DefaultThekla4jBrowserProperties {

  /**
   * Highlight elements values: true, false
   */
  HIGHLIGHT_ELEMENTS(PropertyElement.of("thekla4j.browser.highlightElements", "true", "Possible values: true, false")),

  /**
   * Slow down execution values: true, false
   */
  SLOW_DOWN_EXECUTION(PropertyElement.of("thekla4j.browser.slowDownExecution", "false", "Possible values: true, false")),

  /**
   * Time in seconds to slow down the execution
   */
  SLOW_DOWN_TIME(PropertyElement.of("thekla4j.browser.slowDownTimeInSeconds", "1", "Time in seconds to slow down the execution")),

  /**
   * Auto scroll enabled values: true, false
   */
  AUTO_SCROLL_ENABLED(PropertyElement.of("thekla4j.browser.autoScroll.enabled", "false", "Possible values: true, false")),

  /**
   * Auto scroll vertical values: top, center, bottom
   */
  AUTO_SCROLL_VERTICAL(PropertyElement.of("thekla4j.browser.autoScroll.vertical", "center", "Possible values: top, center, bottom")),

  /**
   * Relative project path to store the screenshots
   */
  SCREENSHOT_RELATIVE_PATH(PropertyElement.of("thekla4j.browser.screenshot.relativePath", "", "Relative project path to store the screenshots")),

  /**
   * Absolute path to store the screenshots
   */
  SCREENSHOT_ABSOLUTE_PATH(
    PropertyElement.of("thekla4j.browser.screenshot.absolutePath", System.getProperty("user.dir"), "Absolute path to store the screenshots"));

  final PropertyElement property;

  /**
   * Get the property
   *
   * @return the property
   */
  public PropertyElement property() {
    return property;
  }

  /**
   * Get the value of the property
   *
   * @return the value of the property
   */
  public String value() {
    return Thekla4jProperty.of(property);
  }

  /**
   * Get the help text for all properties
   *
   * @return the help text for all properties
   */
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
