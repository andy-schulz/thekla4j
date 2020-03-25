package com.teststeps.thekla4j.rest.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.rest.core.RestRequest;
import com.teststeps.thekla4j.rest.spp.Request;

@Action("post to resource: '@{resource}' with options: @{options}")
public class Post extends RequestInteraction<Post> {

    public static Post to(Request request) {
        return new Post(request);
    }

    private Post(Request request) {
        super(request, RestRequest::post);
    }
}