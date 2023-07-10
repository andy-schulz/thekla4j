package com.teststeps.thekla4j.http.core;

import io.vavr.control.Either;

import java.io.File;

public interface HttpRequest {

    Either<Throwable, HttpResult> get();

    Either<Throwable, HttpResult> post();

  Either<Throwable, HttpResult> postFile(File file, String fieldName);

  Either<Throwable, HttpResult> patch();

    Either<Throwable, HttpResult> put();

    Either<Throwable, HttpResult> delete();

}