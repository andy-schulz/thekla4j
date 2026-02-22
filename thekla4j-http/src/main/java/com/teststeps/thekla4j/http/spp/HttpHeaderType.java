package com.teststeps.thekla4j.http.spp;


/**
 * Enum representing standard HTTP header types.
 */
public enum HttpHeaderType {

  /** Accept header for content negotiation */
  ACCEPT("Accept"),
  /** Accept-Encoding header for compression negotiation */
  ACCEPT_ENCODING("Accept-Encoding"),
  /** Content-Type header for specifying media type */
  CONTENT_TYPE("Content-Type"),
  /** Authorization header for authentication */
  AUTHORIZATION("Authorization");

  /** The string representation of the header type */
  public final String asString;

  HttpHeaderType(String type) {
    this.asString = type;
  }
}
