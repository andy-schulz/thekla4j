package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import java.io.File;

/**
 * Activity class for performing HTTP POST requests.
 */
@Action("POST to resource: '@{resource}' with options: @{options}")
public class Post extends RequestInteraction<Post> {

  /**
   * Creates a new POST request activity.
   * 
   * @param request the request configuration
   * @return a new Post instance
   */
  public static Post to(Request request) {
    return new Post(request);
  }

  /**
   * Creates a multipart POST for a file upload.
   * 
   * @param file      the file to upload
   * @param fieldName the form field name
   * @return a PostFileHelper to configure additional parts
   */
  public static PostFile.PostFileHelper file(File file, String fieldName) {
    return PostFile.file(FilePart.of(file, fieldName));
  }

  /**
   * Creates a multipart POST with a FilePart.
   * 
   * @param filePart the file part to upload
   * @return a PostFileHelper to configure additional parts
   */
  public static PostFile.PostFileHelper filePart(FilePart filePart) {
    return PostFile.file(filePart);
  }

  /**
   * Creates a multipart POST with a form Part.
   * 
   * @param part the form part
   * @return a PostFileHelper to configure additional parts
   */
  public static PostFile.PostFileHelper part(Part part) {
    return PostFile.part(part);
  }

  private Post(Request request) {
    super(request, HttpRequest::post);
  }
}