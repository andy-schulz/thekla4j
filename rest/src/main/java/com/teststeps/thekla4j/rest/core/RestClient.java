package com.teststeps.thekla4j.rest.core;

import com.teststeps.thekla4j.rest.spp.RestOptions;
import io.vavr.control.Either;

public interface RestClient {

    Either<Throwable, RestRequest> request(String resource, RestOptions options);

}
