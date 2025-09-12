package com.teststeps.thekla4j.browser.selenium.properties;

import static com.teststeps.thekla4j.core.properties.DefaultThekla4jFunctions.helpText;
import static com.teststeps.thekla4j.core.properties.DefaultThekla4jFunctions.value;

import com.teststeps.thekla4j.commons.properties.PropertyElement;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.properties.DefaultThekla4jProperties;
import io.vavr.control.Option;


/**
 * Default properties for thekla4j browser
 */
public enum DefaultThekla4jSeleniumProperties implements DefaultThekla4jProperties {

  /**
   * The browser configuration to use
   */
  SELENIUM_CONFIG(PropertyElement.of("thekla4j.browser.selenium.config", Option.none(), "The Selenium configuration to use")),
  SELENIUM_BIDI_LOG(PropertyElement.of("thekla4j.browser.selenium.bidi.log", Option.of("false"), "Use WebDriver Bidi to get browser logs"));

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
    return value.apply(property);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Option<String> optionValue() {
    return Thekla4jProperty.of(property);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer asInteger() {
    return Thekla4jProperty.asInteger(property)
        .getOrElseThrow(x -> new RuntimeException("Error getting Integer for property: " + property.name(), x));
  }

  /**
   * Get the help text for all properties
   *
   * @return the help text for all properties
   */
  public static String help() {
    return helpText.apply(DefaultThekla4jSeleniumProperties.values());
  }

  DefaultThekla4jSeleniumProperties(PropertyElement property) {
    this.property = property;
  }
}
