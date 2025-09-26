package com.teststeps.thekla4j.browser.spp.abilities;

import static com.teststeps.thekla4j.browser.core.logListener.LogFunctions.logEntryToStacktraceAttachment;

import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.logListener.BrowserLog;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.persona.UsesAbilities;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

/**
 * Ability to listen to browser logs. The browser used must implement the BrowserLog interface.
 * Use ListenToBrowserLogs.of(browser) to create this ability.
 * Use ListenToBrowserLogs.as(actor) to get this ability from an actor.
 */
@Log4j2(topic = "ListenToBrowserLogs")
public class ListenToBrowserLogs implements Ability {
  private final BrowserLog browserLogger;

  /**
   * Create a new ListenToBrowserLogs ability
   *
   * @param browser - the browser to listen to. Must implement BrowserLog interface.
   * @return - a new ListenToBrowserLogs ability
   */
  public static ListenToBrowserLogs of(Browser browser) {
    return new ListenToBrowserLogs(browser);
  }

  /**
   * Get the ListenToBrowserLogs ability from an actor
   *
   * @param actor - the actor to get the ability from
   * @return - a Try of ListenToBrowserLogs ability
   */
  public static Try<ListenToBrowserLogs> as(UsesAbilities actor) {
    return Try.of(() -> (ListenToBrowserLogs) actor.withAbilityTo(ListenToBrowserLogs.class));
  }

  private ListenToBrowserLogs(Browser browser) {
    if (browser instanceof BrowserLog) {
      browserLogger = (BrowserLog) browser;
      browserLogger.initBrowserLog();
    } else {
      throw new IllegalArgumentException("Browser must implement BrowserLog to use ListenToBrowserLogs ability");
    }
  }

  /**
   * Clean up the browser log listener. Should be called when the ability is no longer needed.
   */
  @Override
  public void destroy() {
    browserLogger.cleanUp()
        .onFailure(x -> log.error("Error cleaning up log entries.", x));
  }

  /**
   * Dump the browser log entries as a list of NodeAttachment objects.
   *
   * @return - a list of NodeAttachment objects representing the browser log entries
   */
  @Override
  public List<NodeAttachment> abilityLogDump() {
    return browserLogger.getLogEntries()
        .map(list -> list.map(logEntryToStacktraceAttachment))
        .onFailure(x -> log.error("Unable to dump log entries", x))
        .map(list -> list.map(att -> (NodeAttachment) att))
        .getOrElse(List.empty());
  }
}
