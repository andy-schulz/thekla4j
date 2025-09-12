package com.teststeps.thekla4j.activityLog.data;

public enum LogAttachmentType {
  IMAGE_PNG("image/png"),
  IMAGE_BASE64("image/png;base64"),
  TEXT_PLAIN("text/plain"),
  STACKTRACE("text/stacktrace"),
  VIDEO_MP4("video/mp4"),
  ;

  private final String mime;

  LogAttachmentType(String mime) {
    this.mime = mime;
  }

  public String mime() {
    return mime;
  }


}
