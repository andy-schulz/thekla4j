package com.teststeps.thekla4j.browser.browserstack.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.spp.activities.ExecuteJavaScript;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;
import lombok.extern.log4j.Log4j2;

import static com.teststeps.thekla4j.core.activities.API.map;

/**
 * Set the status of the current Browserstack session to failed
 */
@Log4j2(topic = "Set Browserstack Status")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("set the status of the current Browserstack session to failed")
public class SetBrowserstackStatus extends BasicInteraction {


  private Executor status;
  private Executor session;

  private boolean failsOnError;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {

    String sessionScript = "browserstack_executor: " + JSON.jStringify(session);
    String statusScript = "browserstack_executor: " + JSON.jStringify(status);

    log.debug("Executing script: \n{}", statusScript);

    if (!failsOnError) {
      return actor.attemptsTo(
          ExecuteJavaScript.onBrowser(sessionScript),
            map(__ -> null),
          ExecuteJavaScript.onBrowser(statusScript))
        .fold(x -> {
          log.error("Failed to set Browserstack status: {}", x.getMessage());
          return Either.right(null);
        }, __ -> Either.right(null));
    } else {
      return actor.attemptsTo(
        ExecuteJavaScript.onBrowser(sessionScript),
        map(__ -> null),

        ExecuteJavaScript.onBrowser(statusScript),
        map(__ -> null));
    }
  }

  /**
   * Set the status to failed
   *
   * @param sessionName - the name of the session
   * @param reason - the reason for the failure
   *
   * @return the activity
   */
  public static SetBrowserstackStatus ofTestCaseToFailed(String sessionName, String reason) {
    return new SetBrowserstackStatus(
      Executor.setSessionStatus(Arguments.failed(reason)),
      Executor.setSessionName(Arguments.named(sessionName)),
      false);
  }

  /**
   * Set the status to passed
   *
   * @param sessionName - the name of the session
   * @return the activity
   */
  public static SetBrowserstackStatus ofTestCaseToPassed(String sessionName) {
    return new SetBrowserstackStatus(
      Executor.setSessionStatus(Arguments.passed()),
      Executor.setSessionName(Arguments.named(sessionName)),
      false);
  }

  /**
   * activate that the task is failing on error
   *
   * @return the activity
   */
  public SetBrowserstackStatus failsOnError() {
    this.failsOnError = true;
    return this;
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
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

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
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
