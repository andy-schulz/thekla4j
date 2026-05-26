package com.teststeps.thekla4j.allure.shared;

import com.teststeps.thekla4j.activityLog.ActivityStatus;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * Maps an {@link ActivityLogNode} tree to native Allure steps.
 *
 * <p>Each Actor's log tree is wrapped in a parent step named after the Actor.
 * The root node (name = "START") is synthetic — only its children become real steps.
 * Root-level attachments are emitted on the actor wrapper step.
 */
public final class ActivityLogAllureMapper {

  private static final DateTimeFormatter TIMESTAMP_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

  private ActivityLogAllureMapper() {
  }

  /**
   * Opens a parent "Activity Log" step. Actor logs should be nested under
   * the returned UUID. Call {@link #closeActivityLogSection} when done.
   *
   * @param lifecycle    the Allure lifecycle
   * @param testCaseUuid the UUID of the current test case
   * @return the UUID of the opened section step
   */
  public static String openActivityLogSection(final AllureLifecycle lifecycle, final String testCaseUuid) {
    final String sectionUuid = UUID.randomUUID().toString();
    final StepResult section = new StepResult()
        .setName("\uD83D\uDCCB Activity Log");
    lifecycle.startStep(testCaseUuid, sectionUuid, section);
    return sectionUuid;
  }

  /**
   * Closes the "Activity Log" section step with the given status.
   *
   * @param lifecycle   the Allure lifecycle
   * @param sectionUuid the UUID returned by {@link #openActivityLogSection}
   * @param status      the aggregate status (worst of all actor statuses)
   */
  public static void closeActivityLogSection(final AllureLifecycle lifecycle, final String sectionUuid, final Status status) {
    lifecycle.updateStep(sectionUuid, step -> step.setStatus(status));
    lifecycle.stopStep(sectionUuid);
  }

  /**
   * Creates a wrapper step for the Actor, emits root-level attachments,
   * then maps each child activity node as a nested Allure step.
   *
   * @param lifecycle    the Allure lifecycle
   * @param testCaseUuid the UUID of the current test case
   * @param actorName    the name of the Actor
   * @param root         the root ActivityLogNode (name = "START")
   */
  public static void mapActivityLogToAllureSteps(final AllureLifecycle lifecycle, final String testCaseUuid, final String actorName, final ActivityLogNode root) {
    if (root == null) {
      return;
    }

    final String actorStepUuid = UUID.randomUUID().toString();
    final StepResult actorStep = new StepResult()
        .setName("Log of " + actorName)
        .setStatus(mapStatus(root.status));

    lifecycle.startStep(testCaseUuid, actorStepUuid, actorStep);

    emitAttachments(lifecycle, root.attachments);
    emitVideoAttachments(lifecycle, root.videoAttachments);

    if (root.activityNodes != null) {
      for (final ActivityLogNode child : root.activityNodes) {
        mapNode(lifecycle, actorStepUuid, child);
      }
    }

    lifecycle.updateStep(actorStepUuid, step -> {
      step.setStart(parseTimestamp(root.startedAt));
      step.setStop(deriveStopTime(root));
    });
    lifecycle.stopStep(actorStepUuid);
  }

  /**
   * Recursively maps a single {@link ActivityLogNode} and its children as nested Allure steps.
   *
   * @param lifecycle  the Allure lifecycle
   * @param parentUuid UUID of the parent step to nest under
   * @param node       the activity log node to map
   */
  private static void mapNode(final AllureLifecycle lifecycle, final String parentUuid, final ActivityLogNode node) {
    if (node == null) {
      return;
    }

    final String stepUuid = UUID.randomUUID().toString();
    final String stepName = buildStepName(node.name, node.description);
    final StepResult step = new StepResult()
        .setName(stepName)
        .setStatus(mapStatus(node.status));

    if (stepName.endsWith("...")) {
      addParameterIfNotBlank(step, "description", node.description);
    }

    addParameterIfNotBlank(step, "input", node.input);
    addParameterIfNotBlank(step, "output", node.output);

    lifecycle.startStep(parentUuid, stepUuid, step);

    emitAttachments(lifecycle, node.attachments);
    emitVideoAttachments(lifecycle, node.videoAttachments);

    if (node.activityNodes != null) {
      for (final ActivityLogNode child : node.activityNodes) {
        mapNode(lifecycle, stepUuid, child);
      }
    }

    lifecycle.updateStep(stepUuid, s -> {
      s.setStart(parseTimestamp(node.startedAt));
      s.setStop(deriveStopTime(node));
    });
    lifecycle.stopStep(stepUuid);
  }

  /**
   * Maps ActivityStatus to Allure Status.
   * {@code running} maps to BROKEN as a fallback for unfinished activities.
   *
   * @param activityStatus the activity status to map
   * @return the corresponding Allure status
   */
  public static Status mapStatus(final ActivityStatus activityStatus) {
    if (activityStatus == null) {
      return Status.BROKEN;
    }
    switch (activityStatus) {
      case passed:
        return Status.PASSED;
      case failed:
        return Status.FAILED;
      case running:
        return Status.BROKEN;
      default:
        return Status.BROKEN;
    }
  }

  /**
   * Returns the worse status using deterministic precedence:
   * FAILED > BROKEN > PASSED/SKIPPED/UNKNOWN.
   *
   * @param current   the current aggregate status
   * @param candidate the new status to compare against
   * @return whichever status is worse according to precedence
   */
  public static Status aggregateWorstStatus(final Status current, final Status candidate) {
    return statusRank(candidate) > statusRank(current) ? candidate : current;
  }

  /**
   * Returns a numeric rank for the given status used by {@link #aggregateWorstStatus}
   * to determine precedence: FAILED (3) > BROKEN (2) > everything else (1), null (0).
   */
  private static int statusRank(final Status status) {
    if (status == null) {
      return 0;
    }
    switch (status) {
      case FAILED:
        return 3;
      case BROKEN:
        return 2;
      default:
        return 1;
    }
  }

  /**
   * Builds a display name for an Allure step by combining name and description.
   * Truncates to 100 characters (with trailing "...") if the combined string is too long.
   *
   * @param name        the activity name (falls back to "unnamed" if {@code null})
   * @param description optional description appended after " - "
   * @return the formatted step name
   */
  public static String buildStepName(final String name, final String description) {
    final String base = name != null ? name : "unnamed";
    if (description == null || description.isEmpty()) {
      return base;
    }
    final String full = base + " - " + description;
    if (full.length() > 100) {
      return full.substring(0, 97) + "...";
    }
    return full;
  }

  /**
   * Adds a parameter to the step result if the value is non-null and non-blank.
   */
  private static void addParameterIfNotBlank(final StepResult step, final String name, final String value) {
    if (value != null && !value.trim().isEmpty()) {
      step.getParameters().add(new Parameter().setName(name).setValue(value));
    }
  }

  /**
   * Emits non-video {@link NodeAttachment}s to the current Allure step.
   * Base64-encoded images are decoded to binary; all other types are emitted as text.
   */
  private static void emitAttachments(final AllureLifecycle lifecycle, final List<NodeAttachment> attachments) {
    if (attachments == null) {
      return;
    }
    for (final NodeAttachment attachment : attachments) {
      if (attachment == null || attachment.content() == null) {
        continue;
      }

      final String name = attachment.name() != null ? attachment.name() : "attachment";
      final LogAttachmentType type = attachment.type();
      final String content = attachment.content();

      if (type == LogAttachmentType.IMAGE_BASE64) {
        try {
          final byte[] decoded = Base64.getDecoder().decode(content);
          lifecycle.addAttachment(name, "image/png", ".png", decoded);
        } catch (IllegalArgumentException e) {
          lifecycle.addAttachment(name, "text/plain", ".txt",
            content.getBytes(StandardCharsets.UTF_8));
        }
      } else {
        final String mime = type != null ? type.mime() : "text/plain";
        final String ext = extensionForType(type);
        lifecycle.addAttachment(name, mime, ext,
          content.getBytes(StandardCharsets.UTF_8));
      }
    }
  }

  /**
   * Emits video {@link NodeAttachment}s as URI-list attachments to the current Allure step.
   */
  private static void emitVideoAttachments(final AllureLifecycle lifecycle, final List<NodeAttachment> videoAttachments) {
    if (videoAttachments == null) {
      return;
    }
    for (final NodeAttachment attachment : videoAttachments) {
      if (attachment == null || attachment.content() == null) {
        continue;
      }
      final String name = attachment.name() != null ? attachment.name() : "video";
      lifecycle.addAttachment(name, "text/uri-list", ".uri",
        attachment.content().getBytes(StandardCharsets.UTF_8));
    }
  }

  /**
   * Returns the file extension string for the given {@link LogAttachmentType}.
   * Defaults to {@code ".txt"} for unknown or {@code null} types.
   */
  private static String extensionForType(final LogAttachmentType type) {
    if (type == null) {
      return ".txt";
    }
    switch (type) {
      case IMAGE_PNG:
        return ".png";
      case IMAGE_BASE64:
        return ".png";
      case TEXT_PLAIN:
        return ".txt";
      case STACKTRACE:
        return ".txt";
      case VIDEO_MP4:
        return ".mp4";
      default:
        return ".txt";
    }
  }

  /**
   * Parses a timestamp string to epoch millis.
   * Falls back to current time if parsing fails.
   *
   * @param timestamp the timestamp string in {@code yyyy-MM-dd HH:mm:ss.SSSSSS} format
   * @return epoch milliseconds
   */
  public static Long parseTimestamp(final String timestamp) {
    if (timestamp == null || timestamp.isEmpty()) {
      return System.currentTimeMillis();
    }
    try {
      final LocalDateTime ldt = LocalDateTime.parse(timestamp, TIMESTAMP_FORMAT);
      return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    } catch (DateTimeParseException e) {
      return System.currentTimeMillis();
    }
  }

  /**
   * Derives the stop time for a node from its duration or {@code endedAt} timestamp.
   * Falls back to the current system time if neither is available.
   */
  private static Long deriveStopTime(final ActivityLogNode node) {
    final Long start = parseTimestamp(node.startedAt);
    if (node.duration != null) {
      return start + node.duration.toMillis();
    }
    if (node.endedAt != null && !"not finished".equals(node.endedAt)) {
      return parseTimestamp(node.endedAt);
    }
    return System.currentTimeMillis();
  }
}
