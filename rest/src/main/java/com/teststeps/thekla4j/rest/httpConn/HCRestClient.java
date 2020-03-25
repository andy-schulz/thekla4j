package com.teststeps.thekla4j.rest.httpConn;

import com.teststeps.thekla4j.rest.core.RestClient;
import com.teststeps.thekla4j.rest.core.RestRequest;
import com.teststeps.thekla4j.rest.spp.RestOptions;
import io.vavr.control.Either;
import io.vavr.control.Try;

public class HCRestClient implements RestClient {

    private RestOptions clientRestOptions;

    public static RestClient using(RestOptions opts) {
        return new HCRestClient(opts);
    }

    @Override
    public Either<Throwable, RestRequest> request(String resource, RestOptions options) {
        final Try<RestRequest> t =
                Try.of(() -> HCRestRequest.on(resource).using(options.mergeOnTopOf(this.clientRestOptions)));

        return t.isSuccess() ? Either.right(t.get()) : Either.left(t.getCause());
    }

    private HCRestClient(RestOptions opts) {
        this.clientRestOptions = opts;
    }
}
