package com.teststeps.thekla4j.http.spp;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

public enum RestHeaderType {

    ACCEPT,
    ACCEPT_ENCODING,
    CONTENT_TYPE,
    AUTHORIZATION;

    public static String mapToString(RestHeaderType ht) {
        return Match(ht).of(
                Case($(ACCEPT), () -> "Accept"),
                Case($(ACCEPT_ENCODING), () -> "Accept-Encoding"),
                Case($(AUTHORIZATION), () -> "Authorization"),
                Case($(CONTENT_TYPE), () -> "Content-Type"),
                Case($(), () -> null));
    }
}
