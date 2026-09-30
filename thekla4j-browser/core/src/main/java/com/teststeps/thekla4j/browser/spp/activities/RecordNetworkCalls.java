package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.network.BrowserNetwork;
import com.teststeps.thekla4j.browser.spp.abilities.ListenToNetworkTraffic;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Record the network calls the browser makes, without interfering with them
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "RecordNetworkCalls")
@Action("record network calls matching '@{urlPattern}'")
public class RecordNetworkCalls extends BasicInteraction {

  /** the pattern recording every call */
  private static final String ALL = "*";

  @Called(name = "urlPattern")
  private String urlPattern;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return ListenToNetworkTraffic.as(actor)
        .onSuccess(__ -> log.info(() -> "Recording network calls matching '%s'".formatted(urlPattern)))
        .flatMap(network -> network.recordCalls(urlPattern))
        .transform(ActivityError.toEither("Error while recording network calls matching " + urlPattern));
  }

  /**
   * Record the network calls whose url matches the given pattern, where * matches any sequence of characters
   *
   * @param urlPattern - the url pattern to record
   * @return - a new RecordNetworkCalls activity
   */
  public static RecordNetworkCalls matching(String urlPattern) {
    return new RecordNetworkCalls(urlPattern);
  }

  /**
   * Record every network call the browser makes
   *
   * @return - a new RecordNetworkCalls activity recording every call
   */
  public static RecordNetworkCalls all() {
    return new RecordNetworkCalls(ALL);
  }

  /**
   * Clear the network calls recorded so far. The recorded url patterns stay in place.
   *
   * @return - the activity clearing the recorded network calls
   */
  public static BasicInteraction clear() {
    return new ClearRecordedNetworkCalls();
  }

  @Log4j2(topic = "RecordNetworkCalls")
  @Action("clear the recorded network calls")
  private static class ClearRecordedNetworkCalls extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return ListenToNetworkTraffic.as(actor)
          .peek(__ -> log.info("Clearing the recorded network calls"))
          .flatMap(BrowserNetwork::clearRecordedCalls)
          .transform(ActivityError.toEither("Error while clearing the recorded network calls"));
    }
  }
}
