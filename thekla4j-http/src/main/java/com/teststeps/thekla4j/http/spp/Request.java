package com.teststeps.thekla4j.http.spp;

import lombok.AllArgsConstructor;
import lombok.With;

@AllArgsConstructor
@With
public class Request {

    // properties
    public final String resource;
    public final String description;
    public final HttpOptions options;

    public static Request on(String resource) {
        return new Request(resource, "", HttpOptions.empty());
    }


    public Request called(String description) {
        return this.withDescription(description);
    }
}
