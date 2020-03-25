package com.teststeps.thekla4j.rest.spp;

import com.teststeps.thekla4j.core.util.LanguageCandy;
import com.teststeps.thekla4j.rest.core.RestClient;
import com.teststeps.thekla4j.rest.core.RestRequest;
import io.vavr.control.Either;

public class Request extends LanguageCandy<Request> {

    // properties
    private String resource;
    private String name;
    private RestOptions options;

    public String resource() {
        return this.resource;
    }

    public static Request on(String resource) {
        return new Request(resource, "", RestOptions.empty());
    }


    public Request called(String name) {
        return getNewRequest()
                .setName(name);
    }

    public RestOptions options() {
        return this.options.clone();
    }

    public Request options(RestOptions opts) {
        return getNewRequest()
                .setOptions(opts);
    }

    public Either<Throwable, RestRequest> send(RestClient client, RestOptions activityOptions) {
        return client.request(this.resource,  activityOptions.mergeOnTopOf(this.options));
    }

    private Request getNewRequest() {
        return new Request(resource, name, options);
    }

    private Request setOptions(RestOptions opts) {
        this.options = opts;
        return this;
    }

    private Request setName(String name) {
        this.name = name;
        return this;
    }

    private Request(final String resource, String name, RestOptions opts) {
        this.resource = resource;
        this.name = name;
        this.options = opts.clone();
    }
}
