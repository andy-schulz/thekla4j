package com.teststeps.thekla4j.browser.selenium;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.not;

import com.teststeps.thekla4j.browser.core.BrowserDetails;
import io.vavr.control.Option;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.ImmutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;

/**
 * Pins the normalisation of selenium session capabilities into driver agnostic BrowserDetails.
 *
 * <p>The capability map is not a map of strings: platformName is a selenium Platform, proxy is a
 * selenium Proxy, process ids are Long and several keys hold nested maps. These tests must fail if
 * the normalisation is ever simplified to a cast.</p>
 */
public class TestCapabilityFunctions {

  private BrowserDetails detailsOf(Map<String, Object> capabilities) {
    return CapabilityFunctions.toBrowserDetails.apply(new ImmutableCapabilities(capabilities));
  }

  private Map<String, Object> chromeLikeCapabilities() {
    Map<String, Object> timeouts = new LinkedHashMap<>();
    timeouts.put("implicit", 0L);
    timeouts.put("pageLoad", 300000L);
    timeouts.put("script", 30000L);

    Map<String, Object> chromeOptions = new LinkedHashMap<>();
    chromeOptions.put("debuggerAddress", "localhost:36699");

    Map<String, Object> capabilities = new LinkedHashMap<>();
    capabilities.put("browserName", "chrome");
    capabilities.put("browserVersion", "145.0.7632.117");
    capabilities.put("platformName", Platform.LINUX);
    capabilities.put("acceptInsecureCerts", false);
    capabilities.put("proxy", new Proxy());
    capabilities.put("timeouts", timeouts);
    capabilities.put("goog:chromeOptions", chromeOptions);
    capabilities.put("goog:processID", 2500804L);
    return capabilities;
  }

  @Test
  @DisplayName("a selenium Platform is normalised to a String instead of throwing a ClassCastException")
  public void platformIsNormalisedToString() {
    BrowserDetails details = detailsOf(chromeLikeCapabilities());

    assertThat("platformName is reported", details.platformName(), not(equalTo(Option.none())));
    assertThat("platformName is a readable string", details.platformName().get(),
      equalToIgnoringCase("linux"));
  }

  @Test
  @DisplayName("a selenium Proxy is normalised to a String instead of leaking a selenium object")
  public void proxyIsNormalisedToString() {
    assertThat("proxy is reported as a string",
      detailsOf(chromeLikeCapabilities()).value("proxy").isDefined(), equalTo(true));
  }

  @Test
  @DisplayName("nested maps are flattened to dotted keys so every leaf stays addressable")
  public void nestedMapsAreFlattened() {
    BrowserDetails details = detailsOf(chromeLikeCapabilities());

    assertThat("timeouts.pageLoad", details.value("timeouts.pageLoad"), equalTo(Option.of("300000")));
    assertThat("timeouts.implicit", details.value("timeouts.implicit"), equalTo(Option.of("0")));
    assertThat("timeouts.script", details.value("timeouts.script"), equalTo(Option.of("30000")));
    assertThat("a vendor prefixed nested key keeps its prefix",
      details.value("goog:chromeOptions.debuggerAddress"), equalTo(Option.of("localhost:36699")));

    assertThat("the enclosing map is not also reported as an opaque blob",
      details.value("timeouts"), equalTo(Option.none()));
  }

  @Test
  @DisplayName("Boolean and Long values are normalised to Strings")
  public void scalarsAreNormalisedToStrings() {
    BrowserDetails details = detailsOf(chromeLikeCapabilities());

    assertThat("a Boolean", details.value("acceptInsecureCerts"), equalTo(Option.of("false")));
    assertThat("a Long", details.value("goog:processID"), equalTo(Option.of("2500804")));
  }

  @Test
  @DisplayName("the typed accessors read the normalised values")
  public void typedAccessors() {
    BrowserDetails details = detailsOf(chromeLikeCapabilities());

    assertThat("browser name", details.browserName(), equalTo(Option.of("chrome")));
    assertThat("browser version", details.browserVersion(), equalTo(Option.of("145.0.7632.117")));
  }

  @Test
  @DisplayName("an empty capability set yields empty details instead of failing")
  public void emptyCapabilities() {
    BrowserDetails details = detailsOf(new LinkedHashMap<>());

    assertThat("no entries", details.entries().size(), equalTo(0));
    assertThat("no browser name", details.browserName(), equalTo(Option.none()));
  }

  @Test
  @DisplayName("an empty nested map is kept as a leaf rather than dropping its key")
  public void emptyNestedMapKeepsItsKey() {
    Map<String, Object> capabilities = new LinkedHashMap<>();
    capabilities.put("timeouts", new LinkedHashMap<String, Object>());

    assertThat("the key survives", detailsOf(capabilities).value("timeouts").isDefined(), equalTo(true));
  }
}
