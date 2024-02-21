package com.teststeps.thekla4j.http.commons;

import com.teststeps.thekla4j.utils.json.JSON;
import lombok.With;

import java.time.LocalDateTime;

@With
public record Cookie(
  String name,
    String value,
    String domain,
    String path,
    LocalDateTime expires,
    String sameSite,
    String maxAge,
    boolean httpOnly,
    boolean secure,
    boolean partitioned
) {

  public static Cookie empty() {
    return new Cookie(
        "",
        "",
        null,
        null,
        null,
        null,
        null,
        false,
        false,
        false
    );
  }

  public static Cookie of(String name, String value) {
    return new Cookie(
        name,
        value,
        null,
        null,
        null,
        null,
        null,
        false,
        false,
        false
    );

  }

  public String toString() {
    return JSON.logOf(this);
  }
}
