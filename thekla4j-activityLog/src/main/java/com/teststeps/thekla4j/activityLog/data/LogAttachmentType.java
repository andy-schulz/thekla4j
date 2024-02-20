package com.teststeps.thekla4j.activityLog.data;

import com.teststeps.thekla4j.activityLog.annotations.AttachOnError;
import io.vavr.Function1;

public enum LogAttachmentType {
  IMAGE_PNG("image/png"),
  IMAGE_BASE64("image/png;base64"),
  TEXT_PLAIN("text/plain"),
  ;

  private final String mime;

  LogAttachmentType(String mime) {
    this.mime = mime;
  }

  public String mime() {
    return mime;
  }


}
