package com.teststeps.thekla4j.http.spp;

/**
 * Interface for HTTP header values that can be converted to strings.
 */
public interface HttpHeaderValue {
  /**
   * Returns the string representation of this HTTP header value.
   * 
   * @return the header value as a string
   */
  String asString();
}
