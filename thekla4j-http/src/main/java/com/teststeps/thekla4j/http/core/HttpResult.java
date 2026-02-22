package com.teststeps.thekla4j.http.core;

import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.collection.List;
import io.vavr.collection.Map;

/**
 * Interface representing the result of an HTTP request.
 */
public interface HttpResult {
  /**
   * Returns the HTTP status code.
   * 
   * @return the status code
   */
  Integer statusCode();

  /**
   * Returns the response body as a string.
   * 
   * @return the response body
   */
  String response();

  /**
   * Returns the HTTP response headers.
   * 
   * @return map of header names to header values
   */
  Map<String, List<String>> headers();

  /**
   * Returns the cookies received in the response.
   * 
   * @return list of cookies
   */
  List<Cookie> cookies();

  String toString();

  /**
   * Returns a string representation of the result with indentation.
   * 
   * @param indent the indentation level
   * @return formatted string representation
   */
  String toString(int indent);
}
