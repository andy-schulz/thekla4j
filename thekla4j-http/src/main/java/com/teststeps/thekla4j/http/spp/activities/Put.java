package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.spp.Request;

@Action("PUT to resource: '@{resource}' with options: @{options}")
public class Put extends RequestInteraction<Put> {

  public static Put to(Request request) {
    return new Put(request);
  }

  private Put(Request request) {
    super(request, HttpRequest::put);
  }
}