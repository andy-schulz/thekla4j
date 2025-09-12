package com.teststeps.thekla4j.browser.selenium;

import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_BIDI_LOG;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.control.Option;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class TestSeleniumProperties {

  @AfterEach
  public void cleanUp() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
    System.clearProperty(SELENIUM_BIDI_LOG.property().name());
  }

  @Test
  public void testHelpText() {
    String helpText = DefaultThekla4jSeleniumProperties.help();

    assertThat("check help text contains selenium config", helpText,
      containsString("thekla4j.browser.selenium.config:                 The Selenium configuration to use (default: None)"));
    assertThat("check help text contains selenium bidi log", helpText,
      containsString("thekla4j.browser.selenium.bidi.log:               Use WebDriver Bidi to get browser logs (default: false)"));
  }

  @Test
  public void testSeleniumConfig() {
    RuntimeException exception = assertThrows(RuntimeException.class,
      SELENIUM_CONFIG::value);

    Option<String> value = SELENIUM_CONFIG.optionValue();

    assertThat("correct error message is thrown", exception.getMessage(), equalTo(
      "Property not found: thekla4j.browser.selenium.config. Its a framework problem."));
    assertThat("selenium config is empty", value.isEmpty(), equalTo(true));

  }

  @Test
  public void testSeleniumConfigValue() {

    System.setProperty(SELENIUM_CONFIG.property().name(), "theChangedSeleniumConfig");

    String value = SELENIUM_CONFIG.value();
    Option<String> optionValue = SELENIUM_CONFIG.optionValue();

    assertThat("test that value is read from system property", value,
      equalTo("theChangedSeleniumConfig"));

    assertThat("test option value is defined", optionValue.isDefined(), equalTo(true));
    assertThat("test that value is read from system property", optionValue.get(), equalTo("theChangedSeleniumConfig"));
  }

  @Test
  public void testSeleniumBidiLog() {
    String value = SELENIUM_BIDI_LOG.value();
    Option<String> optionValue = SELENIUM_BIDI_LOG.optionValue();

    assertThat("selenium bidi log has default value", value, equalTo("false"));
    assertThat("selenium bidi log option is defined", optionValue.isDefined(), equalTo(true));
    assertThat("selenium bidi log option has default value", optionValue.get(), equalTo("false"));
  }

  @Test
  public void testSeleniumBidiLogValue() {
    System.setProperty(SELENIUM_BIDI_LOG.property().name(), "true");

    String value = SELENIUM_BIDI_LOG.value();
    Option<String> optionValue = SELENIUM_BIDI_LOG.optionValue();

    assertThat("test that value is read from system property", value, equalTo("true"));
    assertThat("test option value is defined", optionValue.isDefined(), equalTo(true));
    assertThat("test that value is read from system property", optionValue.get(), equalTo("true"));
  }

  @Test
  public void testSeleniumBidiLogCustomValue() {
    System.setProperty(SELENIUM_BIDI_LOG.property().name(), "custom_value");

    String value = SELENIUM_BIDI_LOG.value();
    Option<String> optionValue = SELENIUM_BIDI_LOG.optionValue();

    assertThat("test that custom value is read from system property", value, equalTo("custom_value"));
    assertThat("test option value is defined", optionValue.isDefined(), equalTo(true));
    assertThat("test that custom value is read from system property", optionValue.get(), equalTo("custom_value"));
  }
}
