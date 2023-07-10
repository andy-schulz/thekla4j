package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.spp.Request;

@Action("DELETE from resource: '@{resource}' with options: @{options}")
public class Delete extends RequestInteraction<Delete> {

    public static Delete from(Request request) {
        return new Delete(request);
    }

    private Delete(Request request) {
        super(request, HttpRequest::delete);
    }
}