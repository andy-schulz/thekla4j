package com.teststeps.thekla4j.allure.shared;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Shared reporter that maps actor activity logs to Allure steps.
 */
public final class ActivityLogReporter {

  private ActivityLogReporter() {
  }

  /**
   * Reports actor activity logs as Allure steps under a single "Activity Log" section.
   *
   * @param lifecycle     the Allure lifecycle
   * @param testCaseUuid  the UUID of the current test case
   * @param actors        actors to report
   * @param attachHtmlLog whether to attach HTML logs for each actor
   */
  public static void reportActorLogs(
                                     final AllureLifecycle lifecycle, final String testCaseUuid, final Collection<Actor> actors, final boolean attachHtmlLog
  ) {
    final List<Actor> nonNullActors = actors == null ? List.of() : actors.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (nonNullActors.isEmpty()) {
      return;
    }

    final String sectionUuid = ActivityLogAllureMapper.openActivityLogSection(lifecycle, testCaseUuid);
    Status worstStatus = Status.PASSED;
    for (final Actor actor : nonNullActors) {
      final ActivityLogNode logTree = actor.activityLog.getLogTree();
      ActivityLogAllureMapper.mapActivityLogToAllureSteps(
        lifecycle, sectionUuid, actor.getName(), logTree);

      final Status actorStatus = ActivityLogAllureMapper.mapStatus(logTree != null ? logTree.status : null);
      worstStatus = ActivityLogAllureMapper.aggregateWorstStatus(worstStatus, actorStatus);

      if (attachHtmlLog) {
        final String html = actor.activityLog.getStructuredHtmlLog();
        lifecycle.addAttachment(
          "Activity Log (" + actor.getName() + ")",
          "text/html",
          ".html",
          html.getBytes(StandardCharsets.UTF_8));
      }
    }

    ActivityLogAllureMapper.closeActivityLogSection(lifecycle, sectionUuid, worstStatus);
  }
}
