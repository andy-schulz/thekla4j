package com.teststeps.thekla4j.http.spp;

public enum Accept implements HttpHeaderValue {
  APPLICATION_JAVA_ARCHIVE("application/java-archive"),
  APPLICATION_EDI_X12("application/EDI-X12"),
  APPLICATION_JAVASCRIPT("application/javascript"),
  APPLICATION_XML("application/xml"),
  APPLICATION_PDF("application/pdf"),
  APPLICATION_OCTET_STREAM("application/octet-stream"),
  APPLICATION_OGG("application/ogg"),
  APPLICATION_ZIP("application/zip"),
  APPLICATION_XHTML_XML("application/xhtml+xml"),
  APPLICATION_JSON("application/json"),
  APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
  APPLICATION_ID_JSON("application/ld+json"),
  APPLICATION_EDIFACT("application/EDIFACT");

  private final String asString;

  Accept(String s) {
    this.asString = s;
  }

  @Override
  public String asString() {
    return asString;
  }
}
