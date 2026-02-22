package com.teststeps.thekla4j.http.spp;

/**
 * Enum representing various Accept HTTP header values for content negotiation.
 */
public enum Accept implements HttpHeaderValue {
  /** Java Archive (JAR) file format */
  APPLICATION_JAVA_ARCHIVE("application/java-archive"),
  /** EDI X12 data format */
  APPLICATION_EDI_X12("application/EDI-X12"),
  /** JavaScript code */
  APPLICATION_JAVASCRIPT("application/javascript"),
  /** XML document format */
  APPLICATION_XML("application/xml"),
  /** PDF document format */
  APPLICATION_PDF("application/pdf"),
  /** Binary data stream */
  APPLICATION_OCTET_STREAM("application/octet-stream"),
  /** Ogg multimedia format */
  APPLICATION_OGG("application/ogg"),
  /** ZIP archive format */
  APPLICATION_ZIP("application/zip"),
  /** XHTML document format */
  APPLICATION_XHTML_XML("application/xhtml+xml"),
  /** JSON data format */
  APPLICATION_JSON("application/json"),
  /** URL-encoded form data */
  APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
  /** JSON-LD (Linked Data) format */
  APPLICATION_ID_JSON("application/ld+json"),
  /** EDIFACT data format */
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
