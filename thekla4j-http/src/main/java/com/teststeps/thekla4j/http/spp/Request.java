package com.teststeps.thekla4j.http.spp;

import lombok.With;

/**
 * Represents an HTTP request to be made to a target resource, including the resource URL, a description, and HTTP
 * options.
 */
@With
public class Request {

  /**
   * The target resource URL or path.
   *
   * @param resource the target resource URL or path
   */
  public final String resource;

  /**
   * Human-readable description of the request.
   *
   * @param description a human-readable description of the request
   */
  public final String description;

  /**
   * HTTP options (headers, parameters, body, etc.).
   *
   * @param options the HTTP options
   */
  public final HttpOptions options;

  /**
   * Creates a new Request with the given resource, description and HTTP options.
   *
   * @param resource    the target resource URL or path
   * @param description a human-readable description of the request
   * @param options     the HTTP options for the request, including headers, parameters, body, etc.
   */
  public Request(final String resource, final String description, final HttpOptions options) {
    this.resource = resource;
    this.description = description;
    this.options = options;
  }

  /**
   * Creates a new Request for the given resource URL.
   *
   * @param resource the target resource URL or path
   * @return a new Request instance
   */
  public static Request on(String resource) {
    return new Request(resource, "", HttpOptions.empty());
  }

  /**
   * Sets a human-readable description for this request.
   *
   * @param description the description
   * @return a new Request instance with the description set
   */
  public Request called(String description) {
    return withDescription(description);
  }
}
