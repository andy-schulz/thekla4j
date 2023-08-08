package com.teststeps.thekla4j.http.core;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;

public interface HttpResult {
    Integer statusCode();
    String response();
    HashMap<String, List<String>> headers();
    List<Cookie> cookies();

    String toString();

    String toString(int indent);
}
