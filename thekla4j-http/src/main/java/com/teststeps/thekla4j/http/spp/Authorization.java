package com.teststeps.thekla4j.http.spp;

import java.util.function.Function;

public enum Authorization implements HttpHeaderValue {

  BASIC(x -> "Basic " + x),
  BEARER(x -> "Bearer " + x),;

  private final Function<String, String> headerFunc;
  private String value;

  public final String asString() {
    return headerFunc.apply(value);
  };

  public Authorization of(String value) {
    this.value = value;
    return this;
  }

  private Authorization(Function<String, String> f) {
    this.headerFunc = f;
  }

}
