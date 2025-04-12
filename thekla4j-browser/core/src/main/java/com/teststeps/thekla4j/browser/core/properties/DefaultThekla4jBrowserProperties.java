package com.teststeps.thekla4j.browser.core.properties;

import com.teststeps.thekla4j.commons.properties.PropertyElement;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.properties.DefaultThekla4jProperties;
import io.vavr.control.Option;

import static com.teststeps.thekla4j.core.properties.DefaultThekla4jFunctions.helpText;


/**
 * Default properties for thekla4j browser
 */
public enum DefaultThekla4jBrowserProperties implements DefaultThekla4jProperties {

  /**
   * Highlight elements values: true, false
   */
  HIGHLIGHT_ELEMENTS(PropertyElement.of("thekla4j.browser.highlightElements", Option.of("true"), "Possible values: true, false")),

  /**
   * Slow down execution values: true, false
   */
  SLOW_DOWN_EXECUTION(PropertyElement.of("thekla4j.browser.slowDownExecution", Option.of("false"), "Possible values: true, false")),

  /**
   * Time in seconds to slow down the execution
   */
  SLOW_DOWN_TIME(PropertyElement.of("thekla4j.browser.slowDownTimeInSeconds", Option.of("1"), "Time in seconds to slow down the execution")),

  /**
   * Auto scroll enabled values: true, false
   */
  AUTO_SCROLL_ENABLED(PropertyElement.of("thekla4j.browser.autoScroll.enabled", Option.of("false"), "Possible values: true, false")),

  /**
   * Auto scroll vertical values: top, center, bottom
   */
  AUTO_SCROLL_VERTICAL(PropertyElement.of("thekla4j.browser.autoScroll.vertical", Option.of("center"), "Possible values: top, center, bottom")),

  /**
   * Relative project path to store the screenshots
   */
  SCREENSHOT_RELATIVE_PATH(PropertyElement.of("thekla4j.browser.screenshot.relativePath", Option.of(""), "Relative project path to store the screenshots")),

  /**
   * Absolute path to store the screenshots
   */
  SCREENSHOT_ABSOLUTE_PATH(
    PropertyElement.of("thekla4j.browser.screenshot.absolutePath", Option.of(System.getProperty("user.dir")), "Absolute path to store the screenshots")),

  /**
   * The browser configuration to use
   */
  BROWSER_CONFIG(PropertyElement.of("thekla4j.browser.config", Option.none(), "The browser configuration to use"));

  final PropertyElement property;

  /**
   * {@inheritDoc}
   */
  @Override
  public PropertyElement property() {
    return property;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String value() {
    return Thekla4jProperty.of(property)
      .getOrElseThrow(() -> new RuntimeException("Property not found: " + property.name() + ". Its a framework problem."));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Option<String> optionValue() {
    return Thekla4jProperty.of(property);
  }

  /**
   * Get the help text for all properties
   *
   * @return the help text for all properties
   */
  public static String help() {
    // iterate over all enums and return the help text
    return helpText.apply(DefaultThekla4jBrowserProperties.values());
  }

  DefaultThekla4jBrowserProperties(PropertyElement property) {
    this.property = property;
  }
}
