package com.teststeps.thekla4j.http.core;

import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public interface HttpResult {
  Integer statusCode();

  String response();

  Map<String, List<String>> headers();

  List<Cookie> cookies();

  String toString();

  String toString(int indent);
}
