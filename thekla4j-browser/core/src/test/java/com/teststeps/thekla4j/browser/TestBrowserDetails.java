package com.teststeps.thekla4j.browser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.BrowserDetails;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.GetBrowserDetail;
import com.teststeps.thekla4j.browser.spp.activities.GetBrowserDetails;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestBrowserDetails {

  private Actor actor;

  @Mock
  Browser chromeMock;

  private final Map<String, String> sessionDetails = LinkedHashMap.of(
    "browserName", "chrome",
    "browserVersion", "138.0.7204.94",
    "platformName", "linux",
    "se:cdpVersion", "138.0.7204.94",
    "goog:chromeOptions", "{debuggerAddress=localhost:40851}");

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    actor = Actor.named("Test Actor").whoCan(BrowseTheWeb.with(chromeMock));
  }

  @Test
  @DisplayName("an empty BrowserDetails reports no details")
  public void emptyDetails() {
    BrowserDetails details = BrowserDetails.empty();

    assertThat("no entries", details.entries().size(), equalTo(0));
    assertThat("no browser name", details.browserName(), equalTo(Option.none()));
    assertThat("no unknown value", details.value("se:cdpVersion"), equalTo(Option.none()));
  }

  @Test
  @DisplayName("null entries are treated as no details instead of failing")
  public void nullEntries() {
    assertThat("no entries", BrowserDetails.of(null).entries().size(), equalTo(0));
  }

  @Test
  @DisplayName("the typed accessors read the well known details")
  public void typedAccessors() {
    BrowserDetails details = BrowserDetails.of(sessionDetails);

    assertThat("browser name", details.browserName(), equalTo(Option.of("chrome")));
    assertThat("browser version", details.browserVersion(), equalTo(Option.of("138.0.7204.94")));
    assertThat("platform name", details.platformName(), equalTo(Option.of("linux")));
  }

  @Test
  @DisplayName("vendor prefixed details are readable by name")
  public void vendorPrefixedDetails() {
    BrowserDetails details = BrowserDetails.of(sessionDetails);

    assertThat("cdp version", details.value("se:cdpVersion"), equalTo(Option.of("138.0.7204.94")));
    assertThat("chrome options", details.value("goog:chromeOptions"),
      equalTo(Option.of("{debuggerAddress=localhost:40851}")));
    assertThat("a detail the driver did not report", details.value("moz:profile"), equalTo(Option.none()));
  }

  @Test
  @DisplayName("toString renders the details as yaml and does not throw")
  public void detailsToString() {
    String rendered = BrowserDetails.of(sessionDetails).toString();

    assertThat("contains the browser name", rendered, containsString("chrome"));
    assertThat("contains the browser version", rendered, containsString("138.0.7204.94"));
  }

  @Test
  @DisplayName("GetBrowserDetails returns all details of the session")
  public void getAllDetails() throws ActivityError {
    when(chromeMock.details()).thenReturn(Try.success(BrowserDetails.of(sessionDetails)));

    BrowserDetails details = GetBrowserDetails.ofSession().runAs(actor).get();

    assertThat("browser version", details.browserVersion(), equalTo(Option.of("138.0.7204.94")));
    verify(chromeMock, times(1)).details();
  }

  @Test
  @DisplayName("GetBrowserDetail returns a single detail by name")
  public void getSingleDetail() throws ActivityError {
    when(chromeMock.details()).thenReturn(Try.success(BrowserDetails.of(sessionDetails)));

    assertThat("browser version",
      GetBrowserDetail.named("browserVersion").runAs(actor).get(), equalTo("138.0.7204.94"));
    assertThat("vendor prefixed detail",
      GetBrowserDetail.named("se:cdpVersion").runAs(actor).get(), equalTo("138.0.7204.94"));
  }

  @Test
  @DisplayName("GetBrowserDetail names the available details when the asked for one is missing")
  public void getMissingDetail() {
    when(chromeMock.details()).thenReturn(Try.success(BrowserDetails.of(sessionDetails)));

    Either<ActivityError, String> result = GetBrowserDetail.named("moz:profile").runAs(actor);

    assertTrue(result.isLeft(), "reading a detail the driver does not report fails");
    assertThat("the error names the missing detail", result.getLeft().getMessage(),
      containsString("moz:profile"));
    assertThat("the error lists the available details", result.getLeft().getMessage(),
      containsString("browserVersion"));
  }

  @Test
  @DisplayName("a driver failure is reported as an ActivityError, not thrown")
  public void driverFailure() {
    when(chromeMock.details()).thenReturn(Try.failure(new IllegalStateException("session closed")));

    assertTrue(GetBrowserDetails.ofSession().runAs(actor).isLeft(), "GetBrowserDetails fails");
    assertTrue(GetBrowserDetail.named("browserName").runAs(actor).isLeft(), "GetBrowserDetail fails");
  }
}
