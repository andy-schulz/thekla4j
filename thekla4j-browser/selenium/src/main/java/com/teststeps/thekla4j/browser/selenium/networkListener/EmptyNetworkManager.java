package com.teststeps.thekla4j.browser.selenium.networkListener;

import com.teststeps.thekla4j.browser.core.network.NetworkCall;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

/**
 * An implementation of NetworkManager that does not observe any network traffic.
 * It logs a warning message when network calls are requested.
 */
@Log4j2(topic = "SeleniumNetworkManager")
public class EmptyNetworkManager implements NetworkManager {

  /**
   * Factory method to create an EmptyNetworkManager instance with a custom warning message.
   *
   * @param message the warning message to log when network calls are requested
   * @return a new EmptyNetworkManager instance
   */
  public static EmptyNetworkManager init(String message) {
    return new EmptyNetworkManager(message);
  }

  private final String message;

  private EmptyNetworkManager(String message) {
    this.message = message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> recordCalls(String urlPattern) {
    log.warn(message);
    return Try.success(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<NetworkCall> recordedCalls() {
    log.warn(message);
    return List.empty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> clearRecordedCalls() {
    return Try.success(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> cleanUp() {
    return Try.success(null);
  }
}
