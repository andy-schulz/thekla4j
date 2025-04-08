package com.teststeps.thekla4j.http.spp.multipart;


import com.teststeps.thekla4j.http.spp.ContentType;

public record Part(
  String name,
  String value,
  ContentType contentType) {

  public static Part of(String name, String value) {
    return new Part(name, value, ContentType.TEXT_PLAIN);
  }

  public static Part of(String name, String value, ContentType contentType) {
    return new Part(name, value, contentType);
  }

}
