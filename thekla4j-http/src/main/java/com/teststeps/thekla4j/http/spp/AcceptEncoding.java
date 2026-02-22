package com.teststeps.thekla4j.http.spp;

/**
 * Enum representing various Accept-Encoding HTTP header values for compression negotiation.
 */
public enum AcceptEncoding implements HttpHeaderValue {

  /** GZIP compression format */
  GZIP("gzip"),
  /** DEFLATE compression format */
  DEFLATE("deflate"),
  /** Brotli compression format */
  BR("br"),
  /** No compression (identity encoding) */
  IDENTITY("identity"),
  /** COMPRESS compression format */
  COMPRESS("compress");

  private final String asString;

  AcceptEncoding(String s) {
    this.asString = s;
  }

  @Override
  public String asString() {
    return asString;
  }
}
