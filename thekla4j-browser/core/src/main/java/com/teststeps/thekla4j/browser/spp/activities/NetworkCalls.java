package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.network.BrowserNetwork;
import com.teststeps.thekla4j.browser.core.network.NetworkCall;
import com.teststeps.thekla4j.browser.core.network.UrlPatternMatch;
import com.teststeps.thekla4j.browser.spp.abilities.ListenToNetworkTraffic;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Get the network calls recorded so far
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "NetworkCalls")
@Action("get the recorded network calls")
public class NetworkCalls extends SupplierTask<List<NetworkCall>> {

  @Override
  protected Either<ActivityError, List<NetworkCall>> performAs(Actor actor) {
    return ListenToNetworkTraffic.as(actor)
        .onSuccess(__ -> log.info("Getting the recorded network calls"))
        .flatMap(BrowserNetwork::recordedCalls)
        .transform(TransformTry.toEither(ActivityError::of));
  }

  /**
   * Create a task to get all network calls recorded so far
   *
   * @return - the task to get the recorded network calls
   */
  public static NetworkCalls recorded() {
    return new NetworkCalls();
  }

  /**
   * Create a task to get the recorded network calls whose url matches the given pattern, where * matches any sequence
   * of
   * characters. This filters what was recorded, it does not change what is being recorded.
   *
   * @param urlPattern - the url pattern to filter the recorded calls by
   * @return - the task to get the matching recorded network calls
   */
  public static SupplierTask<List<NetworkCall>> recordedFor(String urlPattern) {
    return new NetworkCallsFor(urlPattern);
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Log4j2(topic = "NetworkCalls")
  @Action("get the recorded network calls matching '@{urlPattern}'")
  private static class NetworkCallsFor extends SupplierTask<List<NetworkCall>> {

    @Called(name = "urlPattern")
    private String urlPattern;

    @Override
    protected Either<ActivityError, List<NetworkCall>> performAs(Actor actor) {
      return ListenToNetworkTraffic.as(actor)
          .onSuccess(__ -> log.info(() -> "Getting the recorded network calls matching '%s'".formatted(urlPattern)))
          .flatMap(BrowserNetwork::recordedCalls)
          .map(calls -> calls.filter(c -> UrlPatternMatch.matches(urlPattern, c.url())))
          .transform(TransformTry.toEither(ActivityError::of));
    }
  }
}
