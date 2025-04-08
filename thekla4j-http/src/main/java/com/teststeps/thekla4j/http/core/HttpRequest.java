package com.teststeps.thekla4j.http.core;

import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import io.vavr.collection.List;
import io.vavr.control.Either;

public interface HttpRequest {

    Either<Throwable, HttpResult> get();

    Either<Throwable, HttpResult> post();

  Either<Throwable, HttpResult> postFile(List<FilePart> fileParts, List<Part> parts);

  Either<Throwable, HttpResult> patch();

    Either<Throwable, HttpResult> put();

    Either<Throwable, HttpResult> delete();

}