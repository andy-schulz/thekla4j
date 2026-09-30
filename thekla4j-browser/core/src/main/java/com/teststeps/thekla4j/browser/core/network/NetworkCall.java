package com.teststeps.thekla4j.browser.core.network;

import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.With;

/**
 * A network call observed by the browser.
 * <p>
 * A call is assembled from two events: the request is known when it is sent, the response fields arrive later. Until
 * the
 * response has completed, all response side components are {@link Option#none()}.
 * <p>
 * There is no response body. WebDriver BiDi reports the size of a response body but never its content, so
 * {@code responseBodySize} is as close as it gets.
 */
@With
public record NetworkCall(
                          /**
                           * the HTTP method of the request
                           *
                           * @param method the HTTP method
                           * @return the HTTP method
                           */
                          String method,
                          /**
                           * the url the request was sent to
                           *
                           * @param url the url
                           * @return the url
                           */
                          String url,
                          /**
                           * the headers the request was sent with
                           *
                           * @param requestHeaders the request headers
                           * @return the request headers
                           */
                          Map<String, String> requestHeaders,
                          /**
                           * the HTTP status code of the response, or none while the response has not completed
                           *
                           * @param status the HTTP status code
                           * @return the HTTP status code
                           */
                          Option<Integer> status,
                          /**
                           * the reason phrase of the response, or none while the response has not completed
                           *
                           * @param statusText the reason phrase
                           * @return the reason phrase
                           */
                          Option<String> statusText,
                          /**
                           * the headers of the response, empty while the response has not completed
                           *
                           * @param responseHeaders the response headers
                           * @return the response headers
                           */
                          Map<String, String> responseHeaders,
                          /**
                           * the mime type of the response, or none while the response has not completed
                           *
                           * @param mimeType the mime type
                           * @return the mime type
                           */
                          Option<String> mimeType,
                          /**
                           * the size of the response body in bytes, or none while the response has not completed. The
                           * body itself is not available over WebDriver BiDi.
                           *
                           * @param responseBodySize the size of the response body
                           * @return the size of the response body
                           */
                          Option<Long> responseBodySize,
                          /**
                           * whether the response was served from the browser cache, or none while the response has not
                           * completed
                           *
                           * @param fromCache whether the response came from the cache
                           * @return whether the response came from the cache
                           */
                          Option<Boolean> fromCache) {

  /**
   * Create a NetworkCall for a request that has been sent but not yet answered.
   *
   * @param method         - the HTTP method of the request
   * @param url            - the url the request was sent to
   * @param requestHeaders - the headers the request was sent with
   * @return a new NetworkCall without any response information
   */
  public static NetworkCall ofRequest(String method, String url, Map<String, String> requestHeaders) {
    return new NetworkCall(method, url, requestHeaders,
                           Option.none(), Option.none(), LinkedHashMap.empty(),
                           Option.none(), Option.none(), Option.none());
  }

  /**
   * Add the response information of a completed response to this call.
   *
   * @param status           - the HTTP status code
   * @param statusText       - the reason phrase
   * @param responseHeaders  - the headers of the response
   * @param mimeType         - the mime type of the response
   * @param responseBodySize - the size of the response body in bytes
   * @param fromCache        - whether the response was served from the browser cache
   * @return a new NetworkCall carrying the response information
   */
  public NetworkCall withResponse(Integer status, String statusText, Map<String, String> responseHeaders, String mimeType, Long responseBodySize, Boolean fromCache) {
    return new NetworkCall(method, url, requestHeaders,
                           Option.of(status), Option.of(statusText), responseHeaders,
                           Option.of(mimeType), Option.of(responseBodySize), Option.of(fromCache));
  }
}
