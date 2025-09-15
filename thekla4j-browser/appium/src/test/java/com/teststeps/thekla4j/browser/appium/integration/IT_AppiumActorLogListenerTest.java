package com.teststeps.thekla4j.browser.appium.integration;

import static com.teststeps.thekla4j.activityLog.data.LogAttachmentType.STACKTRACE;
import static com.teststeps.thekla4j.browser.core.logListener.LogType.UNKNOWN;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_BIDI_LOG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.activityLog.data.StacktraceAttachment;
import com.teststeps.thekla4j.browser.appium.Appium;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.abilities.ListenToBrowserLogs;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_AppiumActorLogListenerTest {

  private final String webPage = "https://selenium.dev/selenium/web/bidi/logEntryAdded.html";

  private static Actor actor;

  Element consoleLog = Element.found(By.css("#consoleLog"));
  Element consoleError = Element.found(By.css("#consoleError"));
  Element jsException = Element.found(By.css("#jsException"));
  Element logWithStacktrace = Element.found(By.css("#logWithStacktrace"));

  @BeforeEach
  public void setupEach() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_BIDI_LOG.property().name());
  }

  @AfterEach
  public void teardown() {
    if (actor != null) {
      actor.cleansStage();
    }
  }

  private Browser appiumBrowser() {
    return Appium.browser().build();
  }

  @Test
  public void testAppiumWebDriverLogListener() throws ActivityError {
    System.setProperty(SELENIUM_BIDI_LOG.property().name(), "false");

    Browser browser = appiumBrowser();
    actor = Actor.named("Appium Actor")
        .whoCan(BrowseTheWeb.with(browser))
        .whoCan(ListenToBrowserLogs.of(browser));

    actor.attemptsTo(
      Navigate.to(webPage),
      Click.on(consoleLog),
      Click.on(consoleError),
      Click.on(jsException),
      Click.on(logWithStacktrace))
        .getOrElseThrow(Function.identity());

    ActivityLogNode log = actor.attachAbilityDumpToLog().activityLog.getLogTree();

    assertThat("the log shall have at least 5 attachments", log.attachments.size(), greaterThanOrEqualTo(5));

    List<NodeAttachment> logAttachments = log.attachments.stream()
        .filter(e -> e.type().equals(STACKTRACE))
        .filter(e -> e.content().contains("logEntryAdded.html"))
        .toList();

    assertThat("the log shall have 4 log attachments", logAttachments.size(), equalTo(4));

    StacktraceAttachment consoleLogAtt = (StacktraceAttachment) logAttachments.get(0);
    StacktraceAttachment consoleErrorAtt = (StacktraceAttachment) logAttachments.get(1);
    StacktraceAttachment jsExceptionAtt = (StacktraceAttachment) logAttachments.get(2);
    StacktraceAttachment logWithStacktraceAtt = (StacktraceAttachment) logAttachments.get(3);

    assertThat("first element is a type UNKNOWN log", consoleLogAtt.logType(), equalTo(UNKNOWN.toString()));
    assertThat("first element should have INFO level", consoleLogAtt.logLevel(), equalTo("INFO"));

    assertThat("second element is a type UNKNOWN log", consoleErrorAtt.logType(), equalTo(UNKNOWN.toString()));
    assertThat("second element should have ERROR level", consoleErrorAtt.logLevel(), equalTo("ERROR"));

    assertThat("third element is a type UNKNOWN log", jsExceptionAtt.logType(), equalTo(UNKNOWN.toString()));
    assertThat("third element should have ERROR level", jsExceptionAtt.logLevel(), equalTo("ERROR"));

    assertThat("fourth element is a type UNKNOWN log", logWithStacktraceAtt.logType(), equalTo(UNKNOWN.toString()));
    assertThat("fourth element should have ERROR level", logWithStacktraceAtt.logLevel(), equalTo("ERROR"));
  }

  @Test
  public void testAppiumDefaultWebDriverLogListener() throws ActivityError {
    // Test with default BIDI setting (no explicit property set)

    Browser browser = appiumBrowser();
    actor = Actor.named("Appium Actor")
        .whoCan(BrowseTheWeb.with(browser))
        .whoCan(ListenToBrowserLogs.of(browser));

    actor.attemptsTo(
      Navigate.to(webPage),
      Click.on(consoleLog),
      Click.on(consoleError),
      Click.on(jsException),
      Click.on(logWithStacktrace))
        .getOrElseThrow(Function.identity());

    ActivityLogNode log = actor.attachAbilityDumpToLog().activityLog.getLogTree();

    assertThat("the log shall have at least 5 attachments", log.attachments.size(), greaterThanOrEqualTo(5));

    List<NodeAttachment> logAttachments = log.attachments.stream()
        .filter(e -> e.type().equals(STACKTRACE))
        .filter(e -> e.content().contains("logEntryAdded.html"))
        .toList();

    assertThat("the log shall have 4 log attachments", logAttachments.size(), equalTo(4));
  }

  @Test
  public void bidiAppiumLogTest() throws ActivityError {
    System.setProperty(SELENIUM_BIDI_LOG.property().name(), "true");

    Browser browser = appiumBrowser();
    actor = Actor.named("Appium Actor")
        .whoCan(BrowseTheWeb.with(browser))
        .whoCan(ListenToBrowserLogs.of(browser));

    actor.attemptsTo(
      Navigate.to(webPage),
      Click.on(consoleLog),
      Click.on(consoleError),
      Click.on(jsException),
      Click.on(logWithStacktrace))
        .getOrElseThrow(Function.identity());

    ActivityLogNode log = actor.attachAbilityDumpToLog().activityLog.getLogTree();

    // currently Bidi does not capture console logs of Appium sessions
    assertThat("the log shall have 1 attachment", log.attachments.size(), equalTo(1));

  }
}
