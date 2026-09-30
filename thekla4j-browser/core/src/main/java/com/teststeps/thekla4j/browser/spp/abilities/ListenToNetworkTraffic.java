package com.teststeps.thekla4j.browser.spp.abilities;

import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.network.BrowserNetwork;
import com.teststeps.thekla4j.browser.core.network.NetworkCall;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.persona.UsesAbilities;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

/**
 * Ability to observe the network traffic of a browser. The browser used must implement the BrowserNetwork interface.
 * Use ListenToNetworkTraffic.of(browser) to create this ability.
 * Use ListenToNetworkTraffic.as(actor) to get the BrowserNetwork from an actor.
 * <p>
 * The ability must be assigned before the actor interacts with the browser for the first time, because it changes the
 * capabilities the browser session is created with.
 */
@Log4j2(topic = "ListenToNetworkTraffic")
public class ListenToNetworkTraffic implements Ability {

  private final BrowserNetwork browserNetwork;

  /**
   * Create a new ListenToNetworkTraffic ability
   *
   * @param browser - the browser to observe. Must implement BrowserNetwork.
   * @return - a new ListenToNetworkTraffic ability
   */
  public static ListenToNetworkTraffic of(Browser browser) {
    return new ListenToNetworkTraffic(browser);
  }

  /**
   * Get the BrowserNetwork from an actor
   *
   * @param actor - the actor to get the ability from
   * @return - a Try of BrowserNetwork
   */
  public static Try<BrowserNetwork> as(UsesAbilities actor) {
    return Try.of(() -> ((ListenToNetworkTraffic) actor.withAbilityTo(ListenToNetworkTraffic.class)).browserNetwork);
  }

  private ListenToNetworkTraffic(Browser browser) {
    if (browser instanceof BrowserNetwork) {
      browserNetwork = (BrowserNetwork) browser;
      browserNetwork.initNetworkListener();
    } else {
      throw new IllegalArgumentException("Browser must implement BrowserNetwork to use ListenToNetworkTraffic ability");
    }
  }

  /**
   * Stop observing the network traffic. Should be called when the ability is no longer needed.
   */
  @Override
  public void destroy() {
    browserNetwork.cleanUpNetworkListener()
        .onFailure(x -> log.error("Error cleaning up the network listener.", x));
  }

  /**
   * Dump the recorded network calls as a list of NodeAttachment objects.
   *
   * @return - a list holding one NodeAttachment with the recorded network calls
   */
  @Override
  public List<NodeAttachment> abilityLogDump() {
    return browserNetwork.recordedCalls()
        .map(calls -> List.<NodeAttachment>of(
          new LogAttachment("networkCalls", calls.map(NetworkCall::toString).mkString("\n"), LogAttachmentType.TEXT_PLAIN)))
        .onFailure(x -> log.error("Unable to dump the recorded network calls", x))
        .getOrElse(List.empty());
  }
}
