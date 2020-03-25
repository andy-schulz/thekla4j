package com.teststeps.thekla4j.rest.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.rest.core.RestRequest;
import com.teststeps.thekla4j.rest.spp.Request;

@Action("post to resource: '@{resource}' with options: @{options}")
public class Get extends RequestInteraction<Get> {

    public static Get from(Request request) {
        return new Get(request);
    }

    private Get(Request request) {
        super(request, RestRequest::get);
    }
}