package com.teststeps.thekla4j.http.core;

public enum HttpVersion {
  HTTP_1_0("HTTP/1.0"),
  HTTP_1_1("HTTP/1.1"),
  HTTP_2("HTTP/2"),
  HTTP_3("HTTP/3");

  private final String version;

  HttpVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }
}
