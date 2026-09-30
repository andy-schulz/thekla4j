package com.teststeps.thekla4j.browser.selenium.networkListener;

import com.teststeps.thekla4j.browser.core.network.NetworkCall;
import io.vavr.collection.List;
import io.vavr.control.Try;

/**
 * NetworkManager interface for observing network traffic.
 */
public interface NetworkManager {

  /**
   * Records all network calls whose url matches the given pattern.
   *
   * @param urlPattern the url pattern to record, where * matches any sequence of characters
   * @return a Try indicating success or failure
   */
  Try<Void> recordCalls(String urlPattern);

  /**
   * Retrieves the network calls recorded so far, in the order the requests were sent.
   *
   * @return a list of NetworkCall instances
   */
  List<NetworkCall> recordedCalls();

  /**
   * Clears the recorded network calls.
   *
   * @return a Try indicating success or failure
   */
  Try<Void> clearRecordedCalls();

  /**
   * Cleans up resources used by the NetworkManager.
   *
   * @return a Try indicating success or failure
   */
  Try<Void> cleanUp();
}
