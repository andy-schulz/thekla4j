package com.teststeps.thekla4j.activityLog.data;

/**
 * An attachment to be added to the activity log
 */
public interface NodeAttachment {

  /**
   * The name of the attachment in the log.
   * If not specified, the default name "errorAttachment" will be used.
   *
   * @return the name of the attachment
   */
  String name();

  /**
   * The content of the attachment.
   *
   * @return the content of the attachment
   */
  String content();

  /**
   * The type of the attachment.
   * available types are:
   * - LogAttachmentType.IMAGE_PNG
   * - LogAttachmentType.IMAGE_BASE64
   * - LogAttachmentType.TEXT_PLAIN
   * - LogAttachmentType.STACKTRACE
   * - LogAttachmentType.VIDEO_MP4
   *
   * @return the type of the attachment
   */
  LogAttachmentType type();

  /**
   * Returns a string representation of the attachment.
   *
   * @return a string representation of the attachment
   */
  @Override
  String toString();
}
