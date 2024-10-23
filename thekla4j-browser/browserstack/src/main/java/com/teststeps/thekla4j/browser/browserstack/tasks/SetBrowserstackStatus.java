package com.teststeps.thekla4j.browser.browserstack.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.spp.activities.ExecuteJavaScript;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.With;
import lombok.extern.log4j.Log4j2;

/**
 * Set the status of the current Browserstack session to failed
 */
@Log4j2(topic = "Set Browserstack Status")
@AllArgsConstructor
@Action("set the status of the current Browserstack session to failed")
public class SetBrowserstackStatus extends Interaction<Void, Void> {


  private Executor status;
  private Executor session;

  private boolean failsOnError;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor, Void result) {

    String sessionScript = "browserstack_executor: " + JSON.jStringify(session);
    String statusScript = "browserstack_executor: " + JSON.jStringify(status);

    log.debug("Executing script: \n{}", statusScript);

    if (!failsOnError) {
      return actor.attemptsTo(
          ExecuteJavaScript.onBrowser(sessionScript),
          ExecuteJavaScript.onBrowser(statusScript))
        .recover(x -> {
          log.error("Failed to set Browserstack status: {}", x.getMessage());
          return null;
        });
    } else {
      return actor.attemptsTo(
        ExecuteJavaScript.onBrowser(sessionScript),
        ExecuteJavaScript.onBrowser(statusScript));
    }
  }

  /**
   * Set the status to failed
   *
   * @return the activity
   */
  public static SetBrowserstackStatus ofTestCaseToFailed(String sessionName, String reason) {
    return new SetBrowserstackStatus(
      Executor.setSessionStatus(Arguments.failed(reason)),
      Executor.setSessionName(Arguments.named(sessionName)),
      false);
  }

  public static SetBrowserstackStatus ofTestCaseToPassed(String sessionName) {
    return new SetBrowserstackStatus(
      Executor.setSessionStatus(Arguments.passed()),
      Executor.setSessionName(Arguments.named(sessionName)),
      false);
  }

  public SetBrowserstackStatus failsOnError() {
    this.failsOnError = true;
    return this;
  }

  @AllArgsConstructor
  @With
  private static class Executor {
    public String action;
    public Arguments arguments;

    public static Executor setSessionStatus(Arguments arguments) {
      return new Executor("setSessionStatus", arguments);
    }

    public static Executor setSessionName(Arguments arguments) {
      return new Executor("setSessionName", arguments);
    }
  }

  @AllArgsConstructor
  @With
  private static class Arguments {
    public String status;
    public String reason;
    public String name;

    public static Arguments failed(String reason) {
      return new Arguments("failed", reason, null);
    }

    public static Arguments passed() {
      return new Arguments("passed", null, null);
    }

    public static Arguments named(String name) {
      return new Arguments(null, null, name);
    }
  }
}
