package com.teststeps.thekla4j.http.spp.multipart;

import com.teststeps.thekla4j.http.spp.ContentType;

/**
 * Record representing a named form part for multipart requests.
 * 
 * @param name        the form field name
 * @param value       the field value
 * @param contentType the content type of the part
 */
public record Part(
                   String name,
                   String value,
                   ContentType contentType) {

  /**
   * Creates a Part with default content type {@link ContentType#TEXT_PLAIN}.
   * 
   * @param name  the form field name
   * @param value the field value
   * @return a new Part instance
   */
  public static Part of(String name, String value) {
    return new Part(name, value, ContentType.TEXT_PLAIN);
  }

  /**
   * Creates a Part with an explicit content type.
   * 
   * @param name        the form field name
   * @param value       the field value
   * @param contentType the content type of the part
   * @return a new Part instance
   */
  public static Part of(String name, String value, ContentType contentType) {
    return new Part(name, value, contentType);
  }
}
