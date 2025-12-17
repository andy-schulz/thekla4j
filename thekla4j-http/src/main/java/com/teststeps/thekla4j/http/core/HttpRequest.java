package com.teststeps.thekla4j.http.core;

import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import io.vavr.collection.List;
import io.vavr.control.Try;

public interface HttpRequest {

  Try<HttpResult> get();

  Try<HttpResult> post();

  Try<HttpResult> postFile(List<FilePart> fileParts, List<Part> parts);

  Try<HttpResult> patch();

  Try<HttpResult> put();

  Try<HttpResult> delete();

}