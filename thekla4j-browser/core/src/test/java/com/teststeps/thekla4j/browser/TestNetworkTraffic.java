package com.teststeps.thekla4j.browser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.network.BrowserNetwork;
import com.teststeps.thekla4j.browser.core.network.NetworkCall;
import com.teststeps.thekla4j.browser.spp.abilities.ListenToNetworkTraffic;
import com.teststeps.thekla4j.browser.spp.activities.NetworkCalls;
import com.teststeps.thekla4j.browser.spp.activities.RecordNetworkCalls;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestNetworkTraffic {

  /** a browser that can observe network traffic, as the ability requires */
  private interface NetworkBrowser extends Browser, BrowserNetwork {
  }

  private Actor actor;

  @Mock
  NetworkBrowser browserMock;

  @Mock
  Browser plainBrowserMock;

  private final NetworkCall userCall = NetworkCall
      .ofRequest("GET", "https://api.example.com/v1/user", LinkedHashMap.of("accept", "application/json"))
      .withResponse(200, "OK", LinkedHashMap.of("content-type", "application/json"), "application/json", 42L, false);

  private final NetworkCall assetCall = NetworkCall
      .ofRequest("GET", "https://cdn.example.com/logo.png", LinkedHashMap.empty());

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    when(browserMock.initNetworkListener()).thenReturn(Try.success(null));
    actor = Actor.named("Test Actor").whoCan(ListenToNetworkTraffic.of(browserMock));
  }

  @Test
  public void assigningTheAbilityStartsTheNetworkListener() {
    verify(browserMock, times(1)).initNetworkListener();
  }

  @Test
  public void theAbilityRejectsABrowserThatCannotObserveNetworkTraffic() {
    try {
      ListenToNetworkTraffic.of(plainBrowserMock);
      throw new AssertionError("expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertThat("the message names the interface", e.getMessage(),
        equalTo("Browser must implement BrowserNetwork to use ListenToNetworkTraffic ability"));
    }
  }

  @Test
  public void recordingAPatternIsPassedToTheBrowser() throws ActivityError {
    when(browserMock.recordCalls("https://api.example.com/*")).thenReturn(Try.success(null));

    RecordNetworkCalls.matching("https://api.example.com/*").runAs(actor);

    verify(browserMock, times(1)).recordCalls("https://api.example.com/*");
  }

  @Test
  public void recordingAllCallsUsesTheStarPattern() throws ActivityError {
    when(browserMock.recordCalls("*")).thenReturn(Try.success(null));

    RecordNetworkCalls.all().runAs(actor);

    verify(browserMock, times(1)).recordCalls("*");
  }

  @Test
  public void clearingTheRecordedCallsIsPassedToTheBrowser() throws ActivityError {
    when(browserMock.clearRecordedCalls()).thenReturn(Try.success(null));

    RecordNetworkCalls.clear().runAs(actor);

    verify(browserMock, times(1)).clearRecordedCalls();
  }

  @Test
  public void recordedCallsAreReturned() throws ActivityError {
    when(browserMock.recordedCalls()).thenReturn(Try.success(List.of(userCall, assetCall)));

    List<NetworkCall> calls = NetworkCalls.recorded().runAs(actor).get();

    assertThat("both calls are returned", calls.size(), equalTo(2));
    assertThat("the first call is the api call", calls.get(0).url(), equalTo("https://api.example.com/v1/user"));
  }

  @Test
  public void recordedCallsAreFilteredByUrlPattern() throws ActivityError {
    when(browserMock.recordedCalls()).thenReturn(Try.success(List.of(userCall, assetCall)));

    List<NetworkCall> calls = NetworkCalls.recordedFor("https://api.example.com/*").runAs(actor).get();

    assertThat("only the matching call is returned", calls.size(), equalTo(1));
    assertThat("it is the api call", calls.get(0).url(), equalTo("https://api.example.com/v1/user"));
  }

  @Test
  public void destroyingTheAbilityCleansUpTheNetworkListener() {
    when(browserMock.cleanUpNetworkListener()).thenReturn(Try.success(null));

    ListenToNetworkTraffic.of(browserMock).destroy();

    verify(browserMock, times(1)).cleanUpNetworkListener();
  }

  @Test
  public void aCallWithoutAResponseCarriesNoResponseInformation() {
    assertThat("no status", assetCall.status().isEmpty(), equalTo(true));
    assertThat("no mime type", assetCall.mimeType().isEmpty(), equalTo(true));
    assertThat("no body size", assetCall.responseBodySize().isEmpty(), equalTo(true));
    assertThat("no cache information", assetCall.fromCache().isEmpty(), equalTo(true));
    assertThat("no response headers", assetCall.responseHeaders().isEmpty(), equalTo(true));
  }

  @Test
  public void aCallWithAResponseCarriesTheResponseInformation() {
    assertThat("status", userCall.status().get(), equalTo(200));
    assertThat("status text", userCall.statusText().get(), equalTo("OK"));
    assertThat("mime type", userCall.mimeType().get(), equalTo("application/json"));
    assertThat("body size", userCall.responseBodySize().get(), equalTo(42L));
    assertThat("from cache", userCall.fromCache().get(), equalTo(false));
    assertThat("response header", userCall.responseHeaders().get("content-type").get(), equalTo("application/json"));
    assertThat("the request side is kept", userCall.method(), equalTo("GET"));
    assertThat("the request headers are kept", userCall.requestHeaders().get("accept").get(),
      equalTo("application/json"));
  }
}
