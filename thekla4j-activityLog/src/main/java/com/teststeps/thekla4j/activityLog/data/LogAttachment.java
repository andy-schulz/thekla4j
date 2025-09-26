package com.teststeps.thekla4j.activityLog.data;

import lombok.NonNull;

/**
 * An attachment to be added to the activity log
 */
public record LogAttachment(
                            String name,
                            String content,
                            LogAttachmentType type

) implements NodeAttachment {
  @Override
  public @NonNull String toString() {
    return String.format("Attachment: \nname: %s \ncontent: %s \ntype: %s", name, content, type);
  }
}
