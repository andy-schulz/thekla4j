package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.spp.Request;


@Action("post to resource: '@{resource}' with options: @{options}")
public class Get extends RequestInteraction<Get> {

    public static Get from(Request request) {
        return new Get(request);
    }
    private Get(Request request) {
        super(request, HttpRequest::get);
    }
}