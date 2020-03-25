package com.teststeps.thekla4j.rest.core;

import io.vavr.control.Either;

public interface RestRequest {

    Either<Throwable, RestResult> get();

    Either<Throwable, RestResult> post();

    Either<Throwable, RestResult> patch();

    Either<Throwable, RestResult> put();

    Either<Throwable, RestResult> delete();

}