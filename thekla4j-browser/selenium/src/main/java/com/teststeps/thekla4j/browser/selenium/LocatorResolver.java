package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.locator.Locator;
import com.teststeps.thekla4j.browser.core.locator.LocatorType;
import org.openqa.selenium.By;

/**
 * A resolver for locators
 */
public enum LocatorResolver {

  /**
   * CSS locator resolver
   */
  CSS {
    @Override
    public By resolve(String locator) {
      return By.cssSelector(locator);
    }
  },

  /**
   * ID locator resolver
   */
  ID {
    @Override
    public By resolve(String locator) {
      return By.cssSelector("#" + locator);
    }
  },

  /**
   * XPATH locator resolver
   */
  XPATH {
    @Override
    public By resolve(String locator) {
      return By.xpath(locator);
    }
  },

  /**
   * CLASS locator resolver
   */
  TEXT {
    @Override
    public By resolve(String locator) {
      return By.linkText(locator);
    }
  };

  /**
   * Resolve a locator, is implemented by the enum values
   *
   * @param locator - the locator to resolve
   * @return - the resolved locator
   */
  public abstract By resolve(String locator);

  /**
   * Get the locator resolver from a locator type
   *
   * @param locatorType - the locator type
   * @return - the locator resolver
   */
  public static LocatorResolver from(LocatorType locatorType) {
    return LocatorResolver.valueOf(locatorType.name());
  }

  /**
   * Resolve a locator
   *
   * @param locator - the locator to resolve
   * @return - the resolved locator
   */
  public static By resolve(Locator locator) {
    return LocatorResolver.valueOf(locator.type().name()).resolve(locator.locatorString());
  }
}
