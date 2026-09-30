package com.teststeps.thekla4j.browser.selenium.networkListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.teststeps.thekla4j.browser.core.network.NetworkCall;
import io.vavr.collection.List;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.bidi.module.Network;
import org.openqa.selenium.bidi.network.BeforeRequestSent;
import org.openqa.selenium.bidi.network.ResponseDetails;
import org.openqa.selenium.json.Json;

/**
 * The manager is driven with the very payloads chrome sends over the BiDi websocket, so the test covers the JSON
 * mapping
 * as well as the recording.
 */
public class TestBidiNetworkManager {

  @Mock
  Network networkMock;

  private BidiNetworkManager manager;
  private Consumer<BeforeRequestSent> onRequest;
  private Consumer<ResponseDetails> onResponse;

  private static final String API_URL = "https://api.example.com/v1/user";
  private static final String ASSET_URL = "https://cdn.example.com/logo.png";

  @SuppressWarnings("unchecked")
  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    manager = new BidiNetworkManager(networkMock);

    ArgumentCaptor<Consumer<BeforeRequestSent>> requestCaptor = ArgumentCaptor.forClass(Consumer.class);
    ArgumentCaptor<Consumer<ResponseDetails>> responseCaptor = ArgumentCaptor.forClass(Consumer.class);
    verify(networkMock).onBeforeRequestSent(requestCaptor.capture());
    verify(networkMock).onResponseCompleted(responseCaptor.capture());
    onRequest = requestCaptor.getValue();
    onResponse = responseCaptor.getValue();
  }

  @Test
  public void noInterceptIsEverRegistered() {
    verify(networkMock, times(0)).addIntercept(org.mockito.ArgumentMatchers.any());
  }

  @Test
  public void aRequestIsNotRecordedWithoutAMatchingPattern() {
    onRequest.accept(requestEvent("req-1", "GET", API_URL));

    assertThat("nothing recorded", manager.recordedCalls().size(), equalTo(0));
  }

  @Test
  public void aMatchingRequestIsRecorded() {
    manager.recordCalls("https://api.example.com/*");

    onRequest.accept(requestEvent("req-1", "GET", API_URL));

    List<NetworkCall> calls = manager.recordedCalls();
    assertThat("one call recorded", calls.size(), equalTo(1));
    assertThat("method", calls.get(0).method(), equalTo("GET"));
    assertThat("url", calls.get(0).url(), equalTo(API_URL));
    assertThat("request header is mapped", calls.get(0).requestHeaders().get("accept").get(),
      equalTo("application/json"));
    assertThat("no response yet", calls.get(0).status().isEmpty(), equalTo(true));
  }

  @Test
  public void aNonMatchingRequestIsIgnored() {
    manager.recordCalls("https://api.example.com/*");

    onRequest.accept(requestEvent("req-2", "GET", ASSET_URL));

    assertThat("the asset call is not recorded", manager.recordedCalls().size(), equalTo(0));
  }

  @Test
  public void theResponseIsMergedIntoTheRecordedRequest() {
    manager.recordCalls("*");

    onRequest.accept(requestEvent("req-1", "GET", API_URL));
    onResponse.accept(responseEvent("req-1", API_URL, 200, "OK", "application/json", 42L, false));

    List<NetworkCall> calls = manager.recordedCalls();
    assertThat("still one call, not two", calls.size(), equalTo(1));
    assertThat("status", calls.get(0).status().get(), equalTo(200));
    assertThat("status text", calls.get(0).statusText().get(), equalTo("OK"));
    assertThat("mime type", calls.get(0).mimeType().get(), equalTo("application/json"));
    assertThat("body size", calls.get(0).responseBodySize().get(), equalTo(42L));
    assertThat("from cache", calls.get(0).fromCache().get(), equalTo(false));
    assertThat("the request side survives the merge", calls.get(0).method(), equalTo("GET"));
  }

  @Test
  public void aResponseForAnUnrecordedRequestIsDropped() {
    manager.recordCalls("https://api.example.com/*");

    onResponse.accept(responseEvent("unknown", ASSET_URL, 200, "OK", "image/png", 10L, false));

    assertThat("nothing recorded", manager.recordedCalls().size(), equalTo(0));
  }

  @Test
  public void callsAreReturnedInTheOrderTheRequestsWereSent() {
    manager.recordCalls("*");

    onRequest.accept(requestEvent("req-1", "GET", API_URL));
    onRequest.accept(requestEvent("req-2", "POST", ASSET_URL));

    List<NetworkCall> calls = manager.recordedCalls();
    assertThat("first is the api call", calls.get(0).url(), equalTo(API_URL));
    assertThat("second is the asset call", calls.get(1).url(), equalTo(ASSET_URL));
  }

  @Test
  public void severalPatternsCanBeRecorded() {
    manager.recordCalls("https://api.example.com/*");
    manager.recordCalls("https://cdn.example.com/*");

    onRequest.accept(requestEvent("req-1", "GET", API_URL));
    onRequest.accept(requestEvent("req-2", "GET", ASSET_URL));

    assertThat("both are recorded", manager.recordedCalls().size(), equalTo(2));
  }

  @Test
  public void clearingKeepsThePatternsButDropsTheCalls() {
    manager.recordCalls("*");
    onRequest.accept(requestEvent("req-1", "GET", API_URL));

    manager.clearRecordedCalls();
    assertThat("cleared", manager.recordedCalls().size(), equalTo(0));

    onRequest.accept(requestEvent("req-2", "GET", ASSET_URL));
    assertThat("the pattern is still recording", manager.recordedCalls().size(), equalTo(1));
  }

  @Test
  public void aBrokenEventDoesNotEscapeToTheWebsocketThread() {
    manager.recordCalls("*");

    // a handler that throws would kill the BiDi connection thread, so it must swallow and log
    onRequest.accept(null);

    assertThat("nothing recorded, no exception", manager.recordedCalls().size(), equalTo(0));
  }

  @Test
  public void callsRecordedOnAnotherThreadAreVisibleToTheTestThread() throws InterruptedException {
    manager.recordCalls("*");

    Thread bidiThread = new Thread(() -> {
      for (int i = 0; i < 50; i++)
        onRequest.accept(requestEvent("req-" + i, "GET", API_URL + "/" + i));
    }, "fake BiDi Connection");
    bidiThread.start();
    bidiThread.join();

    assertThat("every call written on the other thread is visible", manager.recordedCalls().size(), equalTo(50));
  }

  @Test
  public void cleanUpClosesTheNetworkModuleAndDropsTheState() {
    manager.recordCalls("*");
    onRequest.accept(requestEvent("req-1", "GET", API_URL));

    manager.cleanUp();

    verify(networkMock, times(1)).close();
    assertThat("state dropped", manager.recordedCalls().size(), equalTo(0));
  }

  // --- event payloads as chrome sends them ------------------------------------

  @SuppressWarnings("unchecked")
  private static BeforeRequestSent requestEvent(String requestId, String method, String url) {
    String json = """
        {"context":"CTX","initiator":{"type":"other"},"intercepts":[],"isBlocked":false,
         "navigation":"NAV","redirectCount":0,
         "request":{"bodySize":0,"cookies":[],"destination":"document",
           "headers":[{"name":"accept","value":{"type":"string","value":"application/json"}}],
           "headersSize":274,"initiatorType":null,"method":"$METHOD","request":"$ID",
           "timings":{"connectEnd":0,"connectStart":0,"dnsEnd":0,"dnsStart":0,"fetchStart":0,
             "redirectEnd":0,"redirectStart":0,"requestStart":0,"requestTime":0,"responseEnd":0,
             "responseStart":0,"timeOrigin":1790752194433,"tlsStart":0},
           "url":"$URL"},
         "timestamp":1790752194433}
        """
        .replace("$METHOD", method)
        .replace("$ID", requestId)
        .replace("$URL", url);

    return BeforeRequestSent.fromJsonMap(new Json().toType(json, Map.class));
  }

  @SuppressWarnings("unchecked")
  private static ResponseDetails responseEvent(String requestId, String url, int status, String statusText, String mimeType, long bodySize, boolean fromCache) {
    String json = """
        {"context":"CTX","isBlocked":false,"navigation":"NAV","redirectCount":0,
         "request":{"bodySize":0,"cookies":[],"destination":"document","headers":[],"headersSize":274,
           "initiatorType":null,"method":"GET","request":"$ID",
           "timings":{"connectEnd":0,"connectStart":0,"dnsEnd":0,"dnsStart":0,"fetchStart":0,
             "redirectEnd":0,"redirectStart":0,"requestStart":0,"requestTime":0,"responseEnd":0,
             "responseStart":0,"timeOrigin":1790752194433,"tlsStart":0},
           "url":"$URL"},
         "response":{"bodySize":$BODY_SIZE,"bytesReceived":100,"content":{"size":$BODY_SIZE},
           "fromCache":$FROM_CACHE,
           "headers":[{"name":"content-type","value":{"type":"string","value":"$MIME"}}],
           "headersSize":50,"mimeType":"$MIME","protocol":"h2","status":$STATUS,"statusText":"$STATUS_TEXT",
           "url":"$URL"},
         "timestamp":1790752194500}
        """
        .replace("$ID", requestId)
        .replace("$URL", url)
        .replace("$BODY_SIZE", Long.toString(bodySize))
        .replace("$FROM_CACHE", Boolean.toString(fromCache))
        .replace("$MIME", mimeType)
        .replace("$STATUS_TEXT", statusText)
        .replace("$STATUS", Integer.toString(status));

    return ResponseDetails.fromJsonMap(new Json().toType(json, Map.class));
  }
}
