package com.teststeps.thekla4j.http.core;

/**
 * Enum representing HTTP protocol versions.
 */
public enum HttpVersion {
  /** HTTP version 1.0 */
  HTTP_1_0("HTTP/1.0"),
  /** HTTP version 1.1 */
  HTTP_1_1("HTTP/1.1"),
  /** HTTP version 2 */
  HTTP_2("HTTP/2"),
  /** HTTP version 3 */
  HTTP_3("HTTP/3");

  private final String version;

  HttpVersion(String version) {
    this.version = version;
  }

  /**
   * Returns the HTTP version string representation.
   * 
   * @return the version string, e.g. "HTTP/1.1"
   */
  public String getVersion() {
    return version;
  }
}
