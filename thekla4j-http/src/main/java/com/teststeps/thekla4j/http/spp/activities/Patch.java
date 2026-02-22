package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.spp.Request;

/**
 * Activity class for performing HTTP PATCH requests.
 */
@Action("PATCH to resource: '@{resource}' with options: @{options}")
public class Patch extends RequestInteraction<Patch> {

  /**
   * Creates a new PATCH request activity.
   * 
   * @param request the request configuration
   * @return a new Patch instance
   */
  public static Patch to(Request request) {
    return new Patch(request);
  }

  private Patch(Request request) {
    super(request, HttpRequest::patch);
  }
}