package com.teststeps.thekla4j.http.spp;


public enum HttpHeaderType {

  ACCEPT("Accept"),
  ACCEPT_ENCODING("Accept-Encoding"),
  CONTENT_TYPE("Content-Type"),
  AUTHORIZATION("Authorization");

  public final String asString;

  HttpHeaderType(String type) {
    this.asString = type;
  }
}
