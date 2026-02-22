package com.teststeps.thekla4j.http.core;

import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import io.vavr.collection.List;
import io.vavr.control.Try;

/**
 * Interface for HTTP request operations.
 */
public interface HttpRequest {

  /**
   * Executes an HTTP GET request.
   * 
   * @return the HTTP result wrapped in a Try
   */
  Try<HttpResult> get();

  /**
   * Executes an HTTP POST request.
   * 
   * @return the HTTP result wrapped in a Try
   */
  Try<HttpResult> post();

  /**
   * Executes an HTTP POST request with multipart file upload.
   * 
   * @param fileParts the file parts to upload
   * @param parts     the additional form parts
   * @return the HTTP result wrapped in a Try
   */
  Try<HttpResult> postFile(List<FilePart> fileParts, List<Part> parts);

  /**
   * Executes an HTTP PATCH request.
   * 
   * @return the HTTP result wrapped in a Try
   */
  Try<HttpResult> patch();

  /**
   * Executes an HTTP PUT request.
   * 
   * @return the HTTP result wrapped in a Try
   */
  Try<HttpResult> put();

  /**
   * Executes an HTTP DELETE request.
   * 
   * @return the HTTP result wrapped in a Try
   */
  Try<HttpResult> delete();

}