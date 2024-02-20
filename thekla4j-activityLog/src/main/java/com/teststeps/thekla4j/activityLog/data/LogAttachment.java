package com.teststeps.thekla4j.activityLog.data;

public record LogAttachment(
    String name,
    String content,
    LogAttachmentType type

) implements NodeAttachment {
  @Override
  public String toString() {
    return String.format("Attachment: \nname: %s \ncontent: %s \ntype: %s", name, content, type);
  }
}
