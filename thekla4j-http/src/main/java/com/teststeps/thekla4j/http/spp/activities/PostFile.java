package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import io.vavr.collection.List;

/**
 * Activity class for performing HTTP multipart POST requests (file and form part uploads).
 */
@Action("POST a file to resource: '@{resource}' with options: @{options}")
public class PostFile extends RequestInteraction<PostFile> {

  /**
   * Creates a PostFileHelper starting with a FilePart.
   * 
   * @param filePart the initial file part
   * @return a PostFileHelper to add more parts and set the target request
   */
  public static PostFileHelper file(FilePart filePart) {
    return new PostFileHelper(filePart);
  }

  /**
   * Creates a PostFileHelper starting with a form Part.
   * 
   * @param part the initial form part
   * @return a PostFileHelper to add more parts and set the target request
   */
  public static PostFileHelper part(Part part) {
    return new PostFileHelper(part);
  }

  private PostFile(List<FilePart> fileParts, List<Part> parts, Request request) {
    super(request, req -> req.postFile(fileParts, parts));
  }

  /**
   * Builder helper for configuring a multipart POST request.
   */
  public static class PostFileHelper {

    private List<FilePart> fileParts = List.empty();
    private List<Part> parts = List.empty();

    /**
     * Adds a form Part to this multipart request.
     * 
     * @param part the form part to add
     * @return this helper instance
     */
    public PostFileHelper add(Part part) {
      this.parts = parts.append(part);
      return this;
    }

    /**
     * Adds a FilePart to this multipart request.
     * 
     * @param part the file part to add
     * @return this helper instance
     */
    public PostFileHelper add(FilePart part) {
      this.fileParts = fileParts.append(part);
      return this;
    }

    /**
     * Sets the target request and creates the PostFile activity.
     * 
     * @param request the request configuration
     * @return a new PostFile instance
     */
    public PostFile to(Request request) {
      return new PostFile(fileParts, parts, request);
    }

    /**
     * Creates a PostFileHelper with an initial FilePart.
     * 
     * @param filePart the initial file part
     */
    PostFileHelper(FilePart filePart) {
      this.fileParts = this.fileParts.append(filePart);
    }

    /**
     * Creates a PostFileHelper with an initial form Part.
     * 
     * @param part the initial form part
     */
    public PostFileHelper(Part part) {
      this.parts = parts.append(part);
    }
  }
}
