package com.teststeps.thekla4j.rest.core;

import com.google.gson.Gson;

public interface RestResult {
    Integer statusCode();
    String response();
}
