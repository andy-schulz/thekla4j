package com.teststeps.thekla4j.http.spp;

public enum AcceptEncoding implements HttpHeaderValue {

  GZIP("gzip"),
  DEFLATE("deflate"),
  BR("br"),
  IDENTITY("identity"),
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
