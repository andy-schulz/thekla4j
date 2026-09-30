package com.teststeps.thekla4j.browser.core.network;

import io.vavr.collection.List;
import io.vavr.control.Try;

/**
 * Interface for observing the network traffic of a browser.
 * <p>
 * Only observation is offered. WebDriver BiDi can block a request and let it be answered or rewritten, but releasing a
 * blocked request is currently broken in the Selenium java client, so stubbing, blocking and rewriting are deliberately
 * not part of this interface. WebDriver BiDi also never exposes a response body, which is why {@link NetworkCall}
 * carries the body size only.
 */
public interface BrowserNetwork {

  /**
   * Starts the network listener for the browser session.
   * <p>
   * Must be called before the browser is used for the first time, because it changes the capabilities the session is
   * created with.
   *
   * @return A Try indicating success or failure.
   */
  Try<Void> initNetworkListener();

  /**
   * Records all network calls whose url matches the given pattern.
   * <p>
   * The pattern is matched against the full url, where {@code *} matches any sequence of characters. A pattern of
   * {@code *} records every call. Calling this method more than once adds further patterns.
   *
   * @param urlPattern - the url pattern to record
   * @return A Try indicating success or failure.
   */
  Try<Void> recordCalls(String urlPattern);

  /**
   * Retrieves the network calls recorded so far, in the order the requests were sent.
   *
   * @return A Try containing a List of NetworkCall objects.
   */
  Try<List<NetworkCall>> recordedCalls();

  /**
   * Clears the recorded network calls. The recorded url patterns stay in place.
   *
   * @return A Try indicating success or failure.
   */
  Try<Void> clearRecordedCalls();

  /**
   * Cleans up the resources used to observe the network traffic.
   *
   * @return A Try indicating success or failure.
   */
  Try<Void> cleanUpNetworkListener();
}
