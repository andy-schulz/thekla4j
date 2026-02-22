package com.teststeps.thekla4j.http.spp.multipart;

import java.io.File;

/**
 * Record representing a file part for multipart form data uploads.
 * 
 * @param fileName  the name of the file
 * @param fieldName the name of the form field
 * @param file      the file to upload
 */
public record FilePart(
                       String fileName,
                       String fieldName,
                       File file
) {

  /**
   * Creates a FilePart from a file and field name.
   * 
   * @param file      the file to upload
   * @param fieldName the name of the form field
   * @return a new FilePart instance
   */
  public static FilePart of(File file, String fieldName) {
    return new FilePart(file.getName(), fieldName, file);
  }
}
