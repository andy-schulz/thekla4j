package com.teststeps.thekla4j.browser.selenium.networkListener;

import com.teststeps.thekla4j.browser.core.network.NetworkCall;
import com.teststeps.thekla4j.browser.core.network.UrlPatternMatch;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.bidi.module.Network;
import org.openqa.selenium.bidi.network.BeforeRequestSent;
import org.openqa.selenium.bidi.network.Header;
import org.openqa.selenium.bidi.network.ResponseData;
import org.openqa.selenium.bidi.network.ResponseDetails;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * NetworkManager implementation that records browser network calls using the WebDriver BiDi protocol.
 * <p>
 * This manager only observes. It never registers an intercept, so no request is ever blocked and page loads are not
 * affected.
 * <p>
 * The BiDi event handlers run on the websocket thread of the BiDi connection, not on the test thread. All state is
 * therefore held in AtomicReferences holding immutable Vavr collections, so appends are atomic against concurrent
 * events
 * and the test thread is guaranteed to see them.
 */
@Log4j2(topic = "SeleniumNetworkManager")
public class BidiNetworkManager implements NetworkManager {

  private final Network network;

  /** recorded calls, keyed by BiDi request id, in the order the requests were sent */
  private final AtomicReference<Map<String, NetworkCall>> calls = new AtomicReference<>(LinkedHashMap.empty());
  /** the url patterns that shall be recorded */
  private final AtomicReference<List<String>> patterns = new AtomicReference<>(List.empty());

  /**
   * Factory method to create a BidiNetworkManager instance.
   *
   * @param driver the RemoteWebDriver instance
   * @return a new BidiNetworkManager instance
   */
  public static BidiNetworkManager init(RemoteWebDriver driver) {
    return new BidiNetworkManager(new Network(driver));
  }

  BidiNetworkManager(Network network) {
    this.network = network;

    network.onBeforeRequestSent(this::recordRequest);
    network.onResponseCompleted(this::recordResponse);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> recordCalls(String urlPattern) {
    return Try.run(() -> patterns.updateAndGet(p -> p.append(urlPattern)))
        .onSuccess(__ -> log.debug(() -> "Recording network calls matching '%s'".formatted(urlPattern)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<NetworkCall> recordedCalls() {
    return calls.get().values().toList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> clearRecordedCalls() {
    return Try.run(() -> calls.set(LinkedHashMap.empty()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> cleanUp() {
    calls.set(LinkedHashMap.empty());
    patterns.set(List.empty());
    return Try.run(network::close);
  }

  private void recordRequest(BeforeRequestSent event) {
    Try.run(() -> {
      String url = event.getRequest().getUrl();

      if (!isRecorded(url))
        return;

      NetworkCall call = NetworkCall.ofRequest(
        event.getRequest().getMethod(),
        url,
        headersOf(event.getRequest().getHeaders()));

      calls.updateAndGet(c -> c.put(event.getRequest().getRequestId(), call));
      log.debug(() -> "Recorded request %s %s".formatted(call.method(), call.url()));
    })
        .onFailure(x -> log.error("Error while recording a network request", x));
  }

  private void recordResponse(ResponseDetails event) {
    Try.run(() -> {
      ResponseData response = event.getResponseData();
      String requestId = event.getRequest().getRequestId();

      calls.updateAndGet(c -> c.get(requestId)
          .map(call -> c.put(requestId, call.withResponse(
            response.getStatus(),
            response.getStatusText(),
            headersOf(response.getHeaders()),
            response.getMimeType(),
            response.getBodySize(),
            response.isFromCache())))
          .getOrElse(c));

      log.debug(() -> "Recorded response %s for %s".formatted(response.getStatus(), response.getUrl()));
    })
        .onFailure(x -> log.error("Error while recording a network response", x));
  }

  private boolean isRecorded(String url) {
    return patterns.get().exists(pattern -> UrlPatternMatch.matches(pattern, url));
  }

  private static Map<String, String> headersOf(java.util.List<Header> headers) {
    return headers == null ? LinkedHashMap.empty() : List.ofAll(headers)
        .foldLeft(LinkedHashMap.<String, String>empty(),
          (map, header) -> map.put(header.getName(), header.getValue().getValue()));
  }
}
