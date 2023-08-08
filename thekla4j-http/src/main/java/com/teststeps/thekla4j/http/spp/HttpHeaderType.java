package com.teststeps.thekla4j.http.spp;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

public enum HttpHeaderType {

    ACCEPT("Accept"),
    ACCEPT_ENCODING("Accept-Encoding"),
    CONTENT_TYPE("Content-Type"),
    AUTHORIZATION("Authorization");

    public final String asString;

    HttpHeaderType(String type) {
        this.asString = type;
    }
}
