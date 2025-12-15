package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.spp.Request;

@Action("PUT to resource: '@{resource}' with options: @{options}")
public class Patch extends RequestInteraction<Patch> {

  public static Patch to(Request request) {
    return new Patch(request);
  }

  private Patch(Request request) {
    super(request, HttpRequest::patch);
  }
}