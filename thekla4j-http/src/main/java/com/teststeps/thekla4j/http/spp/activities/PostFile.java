package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.spp.Request;

import java.io.File;

@Action("POST to resource: '@{resource}' with options: @{options}")
public class PostFile extends RequestInteraction<PostFile> {
  public static PostFileHelper file(File file, String fieldName) {
    return new PostFileHelper(file, fieldName);
  }

  private PostFile(File file, String fieldName, Request request) {
    super(request, req -> req.postFile(file, fieldName));
  }

  public static class PostFileHelper {

    private final File file;
    private final String fieldName;

    public PostFile to(Request request) {
      return new PostFile(file, fieldName, request);
    }

    public PostFileHelper(File file, String fieldName) {
      this.file = file;
      this.fieldName = fieldName;
    }
  }


}