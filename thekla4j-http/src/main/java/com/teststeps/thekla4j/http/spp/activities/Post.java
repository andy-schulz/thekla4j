package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.core.HttpClient;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.spp.Request;

@Action("post to resource: '@{resource}' with options: @{options}")
public class Post extends RequestInteraction<Post> {

    public static Post to(Request request) {
        return new Post(request);
    }

    private Post(Request request) {
        super(request, HttpRequest::post);
    }
}