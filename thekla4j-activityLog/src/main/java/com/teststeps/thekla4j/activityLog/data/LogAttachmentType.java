package com.teststeps.thekla4j.activityLog.data;

/**
 * The type of attachment to be added to the activity log
 *
 */
public enum LogAttachmentType {
  /**
   * PNG image
   */
  IMAGE_PNG("image/png"),
  /**
   * Base64 encoded image
   */
  IMAGE_BASE64("image/png;base64"),
  /**
   * Plain text
   */
  TEXT_PLAIN("text/plain"),
  /**
   * Stacktrace text
   */
  STACKTRACE("text/stacktrace"),
  /**
   * MP4 video
   */
  VIDEO_MP4("video/mp4"),
  ;

  private final String mime;

  LogAttachmentType(String mime) {
    this.mime = mime;
  }

  /**
   * Returns the mime type of the attachment
   *
   * @return the mime type of the attachment
   */
  public String mime() {
    return mime;
  }


}
