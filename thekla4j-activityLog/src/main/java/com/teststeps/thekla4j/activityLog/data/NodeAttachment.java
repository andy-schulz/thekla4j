package com.teststeps.thekla4j.activityLog.data;

import com.google.gson.annotations.Expose;

public interface NodeAttachment {

  String name();

  String content();

  LogAttachmentType type();

  String toString();
}
