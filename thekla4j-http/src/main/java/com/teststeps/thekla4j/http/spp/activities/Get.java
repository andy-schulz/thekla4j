package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.spp.Request;


/**
 * Activity class for performing HTTP GET requests.
 */
@Action("GET from resource: '@{resource}' with options: @{options}")
public class Get extends RequestInteraction<Get> {

  /**
   * Creates a new GET request activity.
   * 
   * @param request the request configuration
   * @return a new Get instance
   */
  public static Get from(Request request) {
    return new Get(request);
  }

  private Get(Request request) {
    super(request, HttpRequest::get);
  }
}