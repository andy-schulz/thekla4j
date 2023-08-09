package com.teststeps.thekla4j.http.core;

import com.teststeps.thekla4j.utils.json.JSON;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@With
public class Cookie {
  public String name;
  public String value;

  public String domain;
  public String path;
  public LocalDateTime expires;
  public String sameSite;
  public String maxAge;
  public boolean httpOnly;
  public boolean secure;
  public boolean partitioned;


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

  public String toString() {
    return JSON.logOf(this);
  }
}
