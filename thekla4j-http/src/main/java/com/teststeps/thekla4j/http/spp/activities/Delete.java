package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.spp.Request;

/**
 * Activity class for performing HTTP DELETE requests.
 */
@Action("DELETE from resource: '@{resource}' with options: @{options}")
public class Delete extends RequestInteraction<Delete> {

  /**
   * Creates a new DELETE request activity.
   * 
   * @param request the request configuration
   * @return a new Delete instance
   */
  public static Delete from(Request request) {
    return new Delete(request);
  }

  private Delete(Request request) {
    super(request, HttpRequest::delete);
  }
}