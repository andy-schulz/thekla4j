package com.teststeps.thekla4j.browser.selenium.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.network.NetworkCall;
import com.teststeps.thekla4j.browser.selenium.SeleniumBrowser;
import com.teststeps.thekla4j.browser.selenium.SeleniumLoader;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.abilities.ListenToNetworkTraffic;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.NetworkCalls;
import com.teststeps.thekla4j.browser.spp.activities.RecordNetworkCalls;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Retry;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.time.Duration;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Observing network traffic against a real browser. Only publicly reachable pages are used, so the test also runs
 * against a remote selenium grid.
 */
public class IT_SeleniumNetworkObservationTest {

  private static Actor actor;

  private final String webPage = "https://www.selenium.dev/selenium/web/clicks.html";
  private final String allSeleniumDev = "https://www.selenium.dev/*";

  @AfterEach
  public void teardown() {
    if (actor != null)
      actor.cleansStage();
  }

  private Browser chrome() {
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.CHROME);
    SeleniumLoader loader = SeleniumLoader.of(browserConfig, Option.none(), Option.none());
    return SeleniumBrowser.load(loader, browserConfig);
  }

  private Actor actorObservingTheNetwork() {
    Browser browser = chrome();
    return Actor.named("Network Actor")
        .whoCan(BrowseTheWeb.with(browser))
        .whoCan(ListenToNetworkTraffic.of(browser));
  }

  @Test
  public void recordAMatchingCallWithItsResponse() throws ActivityError {
    actor = actorObservingTheNetwork();

    List<NetworkCall> calls = actor.attemptsTo(

      RecordNetworkCalls.matching(webPage),

      Navigate.to(webPage),

      Retry.task(NetworkCalls.recordedFor(webPage))
          .until(c -> c.exists(call -> call.status().isDefined()), "the response of the page has been recorded")
          .forAsLongAs(Duration.ofSeconds(10))
          .every(Duration.ofMillis(200)))

        .getOrElseThrow(Function.identity());

    assertThat("the page call was recorded", calls.size(), greaterThan(0));

    NetworkCall pageCall = calls.get(0);
    assertThat("the method is recorded", pageCall.method(), equalTo("GET"));
    assertThat("the url is recorded", pageCall.url(), equalTo(webPage));
    assertThat("the status is recorded", pageCall.status().get(), equalTo(200));
    assertThat("the mime type is recorded", pageCall.mimeType().get(), containsString("text/html"));
    assertThat("the body size is recorded", pageCall.responseBodySize().isDefined(), equalTo(true));
    assertThat("request headers are recorded", pageCall.requestHeaders().isEmpty(), equalTo(false));
  }

  @Test
  public void recordingAllCallsCapturesTheSubResourcesToo() throws ActivityError {
    actor = actorObservingTheNetwork();

    List<NetworkCall> calls = actor.attemptsTo(

      RecordNetworkCalls.all(),

      Navigate.to(webPage),

      Retry.task(NetworkCalls.recorded())
          .until(c -> c.size() > 1, "more than the page itself has been recorded")
          .forAsLongAs(Duration.ofSeconds(10))
          .every(Duration.ofMillis(200)))

        .getOrElseThrow(Function.identity());

    assertThat("the page and at least one sub resource were recorded", calls.size(), greaterThan(1));
    assertThat("every recorded call has a url",
      calls.forAll(c -> c.url() != null && !c.url().isBlank()), equalTo(true));
  }

  @Test
  public void aPatternThatMatchesNothingRecordsNothing() throws ActivityError {
    actor = actorObservingTheNetwork();

    List<NetworkCall> calls = actor.attemptsTo(

      RecordNetworkCalls.matching("https://nothing.example.com/*"),

      Navigate.to(webPage),

      NetworkCalls.recorded())

        .getOrElseThrow(Function.identity());

    assertThat("nothing was recorded", calls.size(), equalTo(0));
  }

  @Test
  public void clearingDropsTheRecordedCallsAndKeepsRecording() throws ActivityError {
    actor = actorObservingTheNetwork();

    actor.attemptsTo(

      RecordNetworkCalls.matching(allSeleniumDev),

      Navigate.to(webPage),

      Retry.task(NetworkCalls.recorded())
          .until(c -> !c.isEmpty(), "the first navigation has been recorded")
          .forAsLongAs(Duration.ofSeconds(10))
          .every(Duration.ofMillis(200)))

        .getOrElseThrow(Function.identity());

    List<NetworkCall> afterClear = actor.attemptsTo(

      RecordNetworkCalls.clear(),

      NetworkCalls.recorded())

        .getOrElseThrow(Function.identity());

    assertThat("the recorded calls were dropped", afterClear.size(), equalTo(0));

    List<NetworkCall> afterSecondNavigation = actor.attemptsTo(

      Navigate.to(webPage),

      Retry.task(NetworkCalls.recorded())
          .until(c -> !c.isEmpty(), "the second navigation has been recorded")
          .forAsLongAs(Duration.ofSeconds(10))
          .every(Duration.ofMillis(200)))

        .getOrElseThrow(Function.identity());

    assertThat("recording continued after the clear", afterSecondNavigation.size(), greaterThan(0));
  }

  @Test
  public void theAbilityHasToBeAssignedBeforeTheBrowserIsUsed() {
    Browser browser = chrome();
    actor = Actor.named("Network Actor").whoCan(BrowseTheWeb.with(browser));

    // using the browser first creates the session, so the capabilities can no longer be changed
    actor.attemptsTo(Navigate.to(webPage));

    Either<ActivityError, Void> result = Actor.named("Late Actor")
        .whoCan(BrowseTheWeb.with(browser))
        .whoCan(ListenToNetworkTraffic.of(browser))
        .attemptsTo(RecordNetworkCalls.all());

    assertThat("recording fails when the ability was assigned too late", result.isLeft(), equalTo(true));
  }
}
