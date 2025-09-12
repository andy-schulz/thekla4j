package com.teststeps.thekla4j.browser.appium.integration;

import static com.teststeps.thekla4j.activityLog.data.LogAttachmentType.STACKTRACE;
import static com.teststeps.thekla4j.browser.core.logListener.LogType.UNKNOWN;
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
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_AppiumActorLogListenerTest {

  private final String webPage = "https://selenium.dev/selenium/web/bidi/logEntryAdded.html";

  private static Actor actor;

  @BeforeAll
  public static void setup() {

    Browser browser = Appium.browser().build();

    actor = Actor.named("Appium Actor")
        .whoCan(BrowseTheWeb.with(browser))
        .whoCan(ListenToBrowserLogs.of(browser));

  }

  @AfterEach
  public void teardown() {
    actor.cleansStage();
  }

  @Test
  public void consoleMessageTest() throws ActivityError {
    Element consoleLog = Element.found(By.css("#consoleLog"));
    Element consoleError = Element.found(By.css("#consoleError"));
    Element jsException = Element.found(By.css("#jsException"));
    Element logWithStacktrace = Element.found(By.css("#logWithStacktrace"));

    actor.attemptsTo(

      Navigate.to(webPage),
      Click.on(consoleLog),
      Click.on(consoleError),
      Click.on(jsException),
      Click.on(logWithStacktrace))

        .getOrElseThrow(Function.identity());

    ActivityLogNode log = actor.attachAbilityDumpToLog().activityLog.getLogTree();
//    String logText = actor.activityLog.getStructuredHtmlLog();
//    System.out.println(logText);

    assertThat("the log shall have 5 attachments", log.attachments.size(), greaterThanOrEqualTo(5));

    List<NodeAttachment> logAttachments = log.attachments.stream()
        .filter(e -> e.type().equals(STACKTRACE))
        .filter(e -> e.content().contains("logEntryAdded.html"))
        .toList();

    assertThat("the log shall have 4 log attachments", logAttachments.size(), equalTo(4));

    StacktraceAttachment consoleLogAtt = (StacktraceAttachment) logAttachments.get(0);
    StacktraceAttachment consoleErrorAtt = (StacktraceAttachment) logAttachments.get(1);
    StacktraceAttachment jsExceptionAtt = (StacktraceAttachment) logAttachments.get(2);
    StacktraceAttachment logWithStacktraceAtt = (StacktraceAttachment) logAttachments.get(3);

    assertThat("first element is a UNKNOWN log", consoleLogAtt.logType(), equalTo(UNKNOWN.toString()));
    assertThat("second element is a UNKNOWN log", consoleErrorAtt.logType(), equalTo(UNKNOWN.toString()));
    assertThat("third element is a UNKNOWN log", jsExceptionAtt.logType(), equalTo(UNKNOWN.toString()));
    assertThat("fourth element is a UNKNOWN log", logWithStacktraceAtt.logType(), equalTo(UNKNOWN.toString()));

  }
}
