package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import io.vavr.collection.List;

@Action("POST  a file to resource: '@{resource}' with options: @{options}")
public class PostFile extends RequestInteraction<PostFile> {
  public static PostFileHelper file(FilePart filePart) {
    return new PostFileHelper(filePart);
  }

  public static PostFileHelper part(Part part) {
    return new PostFileHelper(part);
  }

  private PostFile(List<FilePart> fileParts, List<Part> parts, Request request) {
    super(request, req -> req.postFile(fileParts, parts));
  }

  public static class PostFileHelper {

    private List<FilePart> fileParts = List.empty();
    private List<Part> parts = List.empty();

    public PostFileHelper add(Part part) {
      this.parts = parts.append(part);
      return this;
    }

    public PostFileHelper add(FilePart part) {
      this.fileParts = fileParts.append(part);
      return this;
    }

    public PostFile to(Request request) {
      return new PostFile(fileParts, parts, request);
    }

    PostFileHelper(FilePart filePart) {
      this.fileParts = this.fileParts.append(filePart);
    }

    public PostFileHelper(Part part) {
      this.parts = parts.append(part);
    }
  }
}
