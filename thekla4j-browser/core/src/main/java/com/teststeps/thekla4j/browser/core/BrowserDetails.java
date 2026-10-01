package com.teststeps.thekla4j.browser.core;

import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;

/**
 * The details of a running browser session, as reported by the underlying driver.
 *
 * <p>This is the observed counterpart of
 * {@link com.teststeps.thekla4j.browser.config.BrowserConfig}: a BrowserConfig says what was
 * asked for, a BrowserDetails says what the session actually is. The two can differ - a grid
 * may hand out a different browser version than the one requested.</p>
 *
 * <p>The entries are driver agnostic. A WebDriver based browser fills them from the session
 * capabilities, but nothing in this type assumes capabilities exist.</p>
 *
 * @param entries the detail entries of the session, keyed by name
 */
public record BrowserDetails(

                             /**
                              * the detail entries of the session, keyed by name
                              *
                              * @param entries the detail entries
                              * @return the detail entries
                              */
                             Map<String, String> entries
) {

  /** the name of the browser, e.g. {@code chrome} */
  public static final String BROWSER_NAME = "browserName";
  /** the version of the browser, e.g. {@code 138.0.7204.94} */
  public static final String BROWSER_VERSION = "browserVersion";
  /** the platform the browser runs on, e.g. {@code linux} */
  public static final String PLATFORM_NAME = "platformName";

  /**
   * Create a BrowserDetails from the given entries.
   *
   * @param entries the detail entries of the session
   * @return a new BrowserDetails
   */
  public static BrowserDetails of(Map<String, String> entries) {
    return new BrowserDetails(entries == null ? LinkedHashMap.empty() : entries);
  }

  /**
   * Create an empty BrowserDetails.
   *
   * @return a BrowserDetails without any entry
   */
  public static BrowserDetails empty() {
    return new BrowserDetails(LinkedHashMap.empty());
  }

  /**
   * Get the value of a single detail by name.
   *
   * @param name the name of the detail, e.g. {@code browserVersion} or {@code se:cdpVersion}
   * @return the value of the detail, or none if the driver did not report it
   */
  public Option<String> value(String name) {
    return entries.get(name);
  }

  /**
   * Get the name of the browser.
   *
   * @return the browser name, or none if the driver did not report it
   */
  public Option<String> browserName() {
    return value(BROWSER_NAME);
  }

  /**
   * Get the version of the browser.
   *
   * @return the browser version, or none if the driver did not report it
   */
  public Option<String> browserVersion() {
    return value(BROWSER_VERSION);
  }

  /**
   * Get the platform the browser runs on.
   *
   * @return the platform name, or none if the driver did not report it
   */
  public Option<String> platformName() {
    return value(PLATFORM_NAME);
  }

  /**
   * returns a printable yaml string of the object
   *
   * @return a yaml string
   */
  @Override
  public String toString() {
    return YAML.jStringify(entries.toJavaMap());
  }
}
