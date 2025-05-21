package com.teststeps.thekla4j.browser.selenium;

import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
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
  }

  @Test
  public void testHelpText() {
    String helpText = DefaultThekla4jSeleniumProperties.help();

    assertThat("check help text of selenium properties", helpText,
      equalTo("thekla4j.browser.selenium.config: The Selenium configuration to use (default: None)"));
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
}
