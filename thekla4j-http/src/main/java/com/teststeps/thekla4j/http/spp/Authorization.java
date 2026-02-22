package com.teststeps.thekla4j.http.spp;

import java.util.function.Function;

/**
 * Enum representing various Authorization HTTP header types.
 */
public enum Authorization implements HttpHeaderValue {

  /** Basic authentication scheme */
  BASIC(x -> "Basic " + x),
  /** Bearer token authentication scheme */
  BEARER(x -> "Bearer " + x),;

  private final Function<String, String> headerFunc;
  private String value;

  public final String asString() {
    return headerFunc.apply(value);
  };

  /**
   * Sets the authorization value for this authorization type.
   * 
   * @param value the authorization value (e.g., token or credentials)
   * @return this Authorization instance for method chaining
   */
  public Authorization of(String value) {
    this.value = value;
    return this;
  }

  private Authorization(Function<String, String> f) {
    this.headerFunc = f;
  }

}
