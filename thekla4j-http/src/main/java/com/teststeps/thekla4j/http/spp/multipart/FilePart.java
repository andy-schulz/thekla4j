package com.teststeps.thekla4j.http.spp.multipart;

import java.io.File;

public record FilePart(
                       String fileName,
                       String fieldName,
                       File file
) {

  public static FilePart of(File file, String fieldName) {
    return new FilePart(file.getName(), fieldName, file);
  }
}
