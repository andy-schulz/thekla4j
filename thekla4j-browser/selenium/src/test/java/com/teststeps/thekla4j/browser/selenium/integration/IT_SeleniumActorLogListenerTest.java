package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.activityLog.data.LogAttachmentType.STACKTRACE;
import static com.teststeps.thekla4j.browser.core.logListener.LogType.CONSOLE;
import static com.teststeps.thekla4j.browser.core.logListener.LogType.JAVASCRIPT;
import static com.teststeps.thekla4j.browser.core.logListener.LogType.UNKNOWN;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_BIDI_LOG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.activityLog.data.StacktraceAttachment;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.logListener.BrowserLog;
import com.teststeps.thekla4j.browser.core.logListener.LogLevel;
import com.teststeps.thekla4j.browser.selenium.SeleniumBrowser;
import com.teststeps.thekla4j.browser.selenium.SeleniumLoader;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.abilities.ListenToBrowserLogs;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Option;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_SeleniumActorLogListenerTest {

  private final String webPage = "https://selenium.dev/selenium/web/bidi/logEntryAdded.html";

  private static Actor actor;

  Element consoleLog = Element.found(com.teststeps.thekla4j.browser.core.locator.By.css("#consoleLog"));
  Element consoleError = Element.found(com.teststeps.thekla4j.browser.core.locator.By.css("#consoleError"));
  Element jsException = Element.found(com.teststeps.thekla4j.browser.core.locator.By.css("#jsException"));
  Element logWithStacktrace = Element.found(com.teststeps.thekla4j.browser.core.locator.By.css("#logWithStacktrace"));


  @BeforeEach
  public void setupEach() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_BIDI_LOG.property().name());
  }

  @AfterEach
  public void teardown() {
    actor.cleansStage();
  }


  private Browser chrome() {
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.CHROME);
    SeleniumLoader loader = SeleniumLoader.of(browserConfig, Option.none(), Option.none());
    return SeleniumBrowser.load(loader, browserConfig);
  }

  private Browser firefox() {
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.FIREFOX);
    SeleniumLoader loader = SeleniumLoader.of(browserConfig, Option.none(), Option.none());
    return SeleniumBrowser.load(loader, browserConfig);
  }

  @Test
  public void testChromeWebDriverLogListener() throws ActivityError {
    System.setProperty(SELENIUM_BIDI_LOG.property().name(), "false");

    Browser browser = chrome();
    actor = Actor.named("Selenium Actor")
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


    assertThat("the log shall have 5 attachments", log.attachments.size(), equalTo(6));

    List<NodeAttachment> logAttachments = log.attachments.stream().filter(e -> e.type().equals(STACKTRACE)).toList();
    assertThat("the log shall have 4 log attachments", logAttachments.size(), equalTo(5));

    StacktraceAttachment consoleLogAtt = (StacktraceAttachment) logAttachments.get(1);
    StacktraceAttachment consoleErrorAtt = (StacktraceAttachment) logAttachments.get(2);
    StacktraceAttachment jsExceptionAtt = (StacktraceAttachment) logAttachments.get(3);
    StacktraceAttachment logWithStacktraceAtt = (StacktraceAttachment) logAttachments.get(4);

    assertThat("first element is a type UNKNOWN log", consoleLogAtt.logType(), equalTo(UNKNOWN.toString()));
    assertThat("first element is a level INFO log", consoleLogAtt.logLevel(), equalTo("INFO"));

    assertThat("second element is a type UNKNOWN log", consoleErrorAtt.logType(), equalTo(UNKNOWN.toString()));
    assertThat("second element is a level ERROR log", consoleErrorAtt.logLevel(), equalTo("ERROR"));

    assertThat("third element is a type UNKNOWN log", jsExceptionAtt.logType(), equalTo(UNKNOWN.toString()));
    assertThat("third element is a level ERROR log", jsExceptionAtt.logLevel(), equalTo("ERROR"));

    assertThat("fourth element is a type UNKNOWN log", logWithStacktraceAtt.logType(), equalTo(UNKNOWN.toString()));
    assertThat("fourth element is a level ERROR log", logWithStacktraceAtt.logLevel(), equalTo("ERROR"));

  }


  @Test
  public void bidiChromeLogTest() throws ActivityError {

    System.setProperty(SELENIUM_BIDI_LOG.property().name(), "true");

    Browser browser = chrome();
    actor = Actor.named("Selenium Actor")
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

    assertThat("the log shall have 5 attachments", log.attachments.size(), equalTo(5));

    List<NodeAttachment> logAttachments = log.attachments.stream().filter(e -> e.type().equals(STACKTRACE)).toList();
    assertThat("the log shall have 4 log attachments", logAttachments.size(), equalTo(4));

    StacktraceAttachment consoleLogAtt = (StacktraceAttachment) logAttachments.get(0);
    StacktraceAttachment consoleErrorAtt = (StacktraceAttachment) logAttachments.get(1);
    StacktraceAttachment jsExceptionAtt = (StacktraceAttachment) logAttachments.get(2);
    StacktraceAttachment logWithStacktraceAtt = (StacktraceAttachment) logAttachments.get(3);


    assertThat("first element is a CONSOLE log", consoleLogAtt.logType(), equalTo(CONSOLE.toString()));
    assertThat("second element is a CONSOLE log", consoleErrorAtt.logType(), equalTo(CONSOLE.toString()));
    assertThat("third element is a JAVASCRIPT log", jsExceptionAtt.logType(), equalTo(JAVASCRIPT.toString()));
    assertThat("fourth element is a JAVASCRIPT log", logWithStacktraceAtt.logType(), equalTo(JAVASCRIPT.toString()));

  }

  @Test
  public void bidiFirefoxLogTest() throws ActivityError {

    System.setProperty(SELENIUM_BIDI_LOG.property().name(), "true");

    Browser browser = firefox();
    actor = Actor.named("Selenium Actor")
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

    assertThat("the log shall have 5 attachments", log.attachments.size(), equalTo(5));

    List<NodeAttachment> logAttachments = log.attachments.stream().filter(e -> e.type().equals(STACKTRACE)).toList();
    assertThat("the log shall have 4 log attachments", logAttachments.size(), equalTo(4));

    StacktraceAttachment consoleLogAtt = (StacktraceAttachment) logAttachments.get(0);
    StacktraceAttachment consoleErrorAtt = (StacktraceAttachment) logAttachments.get(1);
    StacktraceAttachment jsExceptionAtt = (StacktraceAttachment) logAttachments.get(2);
    StacktraceAttachment logWithStacktraceAtt = (StacktraceAttachment) logAttachments.get(3);


    assertThat("first element is a CONSOLE log", consoleLogAtt.logType(), equalTo(CONSOLE.toString()));
    assertThat("second element is a CONSOLE log", consoleErrorAtt.logType(), equalTo(CONSOLE.toString()));
    assertThat("third element is a JAVASCRIPT log", jsExceptionAtt.logType(), equalTo(JAVASCRIPT.toString()));
    assertThat("fourth element is a JAVASCRIPT log", logWithStacktraceAtt.logType(), equalTo(JAVASCRIPT.toString()));

  }

  @Test
  public void testFirefoxWebDriverLogListener() throws ActivityError {
    System.setProperty(SELENIUM_BIDI_LOG.property().name(), "false");

    Browser browser = firefox();
    actor = Actor.named("Selenium Actor")
        .whoCan(BrowseTheWeb.with(browser))
        .whoCan(ListenToBrowserLogs.of(browser));

    actor.attemptsTo(

      Navigate.to(webPage),
      Click.on(consoleLog),
      Click.on(consoleError),
      Click.on(jsException),
      Click.on(logWithStacktrace))

        .getOrElseThrow(Function.identity());

    BrowserLog logger = (BrowserLog) browser;

    assertThat(logger.getLogEntries(), equalTo(io.vavr.control.Try.success(io.vavr.collection.List.empty())));

  }

  @Test
  public void testChromeDefaultWebDriverLogListener() throws ActivityError {

    Browser browser = chrome();
    actor = Actor.named("Selenium Actor")
        .whoCan(BrowseTheWeb.with(browser))
        .whoCan(ListenToBrowserLogs.of(browser));

    actor.attemptsTo(

      Navigate.to(webPage),
      Click.on(consoleLog),
      Click.on(consoleError),
      Click.on(jsException),
      Click.on(logWithStacktrace))

        .getOrElseThrow(Function.identity());

    BrowserLog logger = (BrowserLog) browser;

    assertThat(logger.getLogEntries().get().size(), equalTo(5));
    assertThat(logger.getLogEntries().get().get(1).getType(), equalTo(UNKNOWN));
    assertThat(logger.getLogEntries().get().get(1).getLevel(), equalTo(LogLevel.INFO));

  }

}
