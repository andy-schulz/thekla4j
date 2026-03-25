package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;
import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.LogStackFrameAttachment;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.activityLog.data.StacktraceAttachment;
import io.vavr.collection.List;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import org.junit.jupiter.api.Test;

/**
 * Generates docs/readme/activityLog_showcase.html
 *
 * Demonstrates every major ActivityLog capability:
 * - All node types: Task, Interaction, Group
 * - All statuses: passed, failed, running
 * - All attachments: TEXT_PLAIN, IMAGE_BASE64, STACKTRACE (with frames), VIDEO_MP4
 * - Input / Output on nodes
 * - Long description (> 100 chars → expand "..." button)
 * - Multi-level nesting
 * - Multiple root nodes rendered in one report (List overload)
 * - TheklaActivityLog fluent API as an alternative to raw ActivityLogNode construction
 *
 * Run manually whenever the HTML format changes to keep the showcase up to date.
 */
class ActivityLogShowcaseTest {

  // ── image helper ─────────────────────────────────────────────────────────────

  private String redDotBase64() throws IOException {
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("reddot.png")) {
      if (in == null) return "";
      return Base64.getEncoder().encodeToString(in.readAllBytes());
    }
  }

  // ── Scenario 1: Login — all-passed, nested Task → Task → Interaction ─────────

  private TheklaActivityLog buildLoginScenario() {
    TheklaActivityLog log = new TheklaActivityLog("Alice");

    ActivityLogEntry openBrowser = log.addActivityLogEntry(
      "OpenBrowser", "open Chrome browser",
      ActivityLogEntryType.Task, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    openBrowser.setInput("browser: Chrome, headless: false");
    openBrowser.setOutput("browser session started");

    ActivityLogEntry navigateInteraction = log.addActivityLogEntry(
      "Navigate", "navigate to https://app.example.com/login",
      ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    navigateInteraction.setInput("https://app.example.com/login");
    navigateInteraction.setOutput("HTTP 200 OK");
    log.reset(navigateInteraction);

    log.reset(openBrowser);

    ActivityLogEntry loginTask = log.addActivityLogEntry(
      "Login", "log in with valid credentials",
      ActivityLogEntryType.Task, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    loginTask.setInput("username: alice@example.com, password: ••••••••");

    ActivityLogEntry fillUser = log.addActivityLogEntry(
      "Fill", "fill username field",
      ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    fillUser.setInput("alice@example.com");
    log.reset(fillUser);

    ActivityLogEntry fillPass = log.addActivityLogEntry(
      "Fill", "fill password field",
      ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    fillPass.setInput("••••••••");
    log.reset(fillPass);

    ActivityLogEntry clickLogin = log.addActivityLogEntry(
      "Click", "click the 'Login' button",
      ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    clickLogin.setOutput("redirect → /dashboard");
    log.reset(clickLogin);

    loginTask.setOutput("logged in, redirected to /dashboard");
    log.reset(loginTask);

    return log;
  }

  // ── Scenario 2: Search — mixed passed/failed, long desc, attachments ─────────

  private TheklaActivityLog buildSearchScenario(String redDot) {
    TheklaActivityLog log = new TheklaActivityLog("Bob");

    // Group: pre-conditions (Group node type — no I/O, container only)
    ActivityLogEntry preConditions = log.addActivityLogEntry(
      "PreConditions", "verify dashboard is ready",
      ActivityLogEntryType.Group, TASK_LOG.NO_INPUT_OUTPUT, ActivityStatus.passed);

    ActivityLogEntry checkTitle = log.addActivityLogEntry(
      "Assert", "assert page title equals 'Dashboard | MyApp'",
      ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    checkTitle.setInput("expected: 'Dashboard | MyApp'");
    checkTitle.setOutput("actual:   'Dashboard | MyApp'");
    log.reset(checkTitle);

    ActivityLogEntry checkUrl = log.addActivityLogEntry(
      "Assert", "assert URL contains '/dashboard'",
      ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    checkUrl.setInput("expected contains: '/dashboard'");
    checkUrl.setOutput("actual: 'https://app.example.com/dashboard'");
    log.reset(checkUrl);

    log.reset(preConditions);

    // Task: perform search — long description (> 100 chars)
    String longDesc = "search for the product 'Wireless Noise-Cancelling Headphones XL 2024 Edition' " +
        "in the global search bar and verify at least one result is returned matching the query terms";

    ActivityLogEntry searchTask = log.addActivityLogEntry(
      "SearchProduct", longDesc,
      ActivityLogEntryType.Task, TASK_LOG.FULL_LOG, ActivityStatus.failed);
    searchTask.setInput("query: 'Wireless Noise-Cancelling Headphones XL 2024 Edition'");

    ActivityLogEntry typeQuery = log.addActivityLogEntry(
      "Fill", "type search query into search field",
      ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    typeQuery.setInput("Wireless Noise-Cancelling Headphones XL 2024 Edition");
    log.reset(typeQuery);

    ActivityLogEntry pressEnter = log.addActivityLogEntry(
      "PressKey", "press Enter to submit the search",
      ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    pressEnter.setInput("key: Enter");
    pressEnter.setOutput("search results page loaded");
    log.reset(pressEnter);

    // TEXT_PLAIN attachment on failed node
    NodeAttachment errorText = new LogAttachment(
                                                 "assertion-error",
                                                 "AssertionError: expected result count >= 1 but was 0\n" +
                                                     "  Query:    'Wireless Noise-Cancelling Headphones XL 2024 Edition'\n" +
                                                     "  Expected: count >= 1\n" + "  Actual:   count  = 0",
                                                 LogAttachmentType.TEXT_PLAIN);

    // STACKTRACE attachment with JS frames
    StacktraceAttachment jsError = StacktraceAttachment.of(
      LocalDateTime.of(2026, 3, 24, 10, 0, 1, 500_000_000),
      "console-error",
      "ERROR", "javascript",
      "TypeError: Cannot read properties of null (reading 'length')",
      List.of(
        new LogStackFrameAttachment("processResults", "search.bundle.js", 142, 18),
        new LogStackFrameAttachment("onSearchResponse", "search.bundle.js", 89, 5),
        new LogStackFrameAttachment("dispatchEvent", "event-hub.js", 33, 12)));

    // IMAGE_BASE64 screenshot
    NodeAttachment screenshot = new LogAttachment(
                                                  "failure-screenshot", redDot, LogAttachmentType.IMAGE_BASE64);

    ActivityLogEntry assertResults = log.addActivityLogEntry(
      "Assert", "verify at least one search result is displayed",
      ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.failed);
    assertResults.setInput("expectedMinCount: 1");
    assertResults.setOutput("AssertionError: expected >= 1 results but got 0");
    assertResults.appendAttachment(errorText);
    assertResults.appendAttachment(jsError);
    assertResults.appendAttachment(screenshot);
    log.reset(assertResults);

    searchTask.setOutput("search assertion failed — 0 results returned");
    log.reset(searchTask);

    return log;
  }

  // ── Scenario 3: Upload report — running (in-progress) + VIDEO_MP4 ────────────

  private TheklaActivityLog buildUploadScenario() {
    TheklaActivityLog log = new TheklaActivityLog("Charlie");

    // Video attached to root node
    log.appendVideoAttachmentToRootNode(new LogAttachment(
                                                          "test-recording",
                                                          "https://file-examples.com/storage/fea570b16e6703ef79e65b4/2017/04/file_example_MP4_480_1_5MG.mp4",
                                                          LogAttachmentType.VIDEO_MP4));

    ActivityLogEntry uploadTask = log.addActivityLogEntry(
      "UploadReport", "compress and upload the test artefacts to the CI server",
      ActivityLogEntryType.Task, TASK_LOG.FULL_LOG, ActivityStatus.running);
    uploadTask.setInput("artefacts: [report.html, screenshots/, logs/]");

    ActivityLogEntry zipStep = log.addActivityLogEntry(
      "Compress", "zip artefact directory",
      ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    zipStep.setInput("source: ./build/reports");
    zipStep.setOutput("report.zip (4.2 MB)");
    log.reset(zipStep);

    // still running
    log.addActivityLogEntry(
      "Upload", "POST report.zip to https://ci.example.com/upload",
      ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.running);
    // intentionally not reset — still in progress

    return log;
  }

  // ── generator ────────────────────────────────────────────────────────────────

  @Test
  void generateShowcase() throws IOException, URISyntaxException {
    String redDot = redDotBase64();

    TheklaActivityLog login = buildLoginScenario();
    TheklaActivityLog search = buildSearchScenario(redDot);
    TheklaActivityLog upload = buildUploadScenario();

    // Render three separate scenario logs into one report (List overload)
    String fragment = TheklaActivityLog.getStructuredHtmlListLog(
      List.of(login, search, upload));

    String html = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>ActivityLog Showcase</title>
        </head>
        <body>
        """ + fragment + """
        </body>
        </html>
        """;

    // Resolve output path relative to module root
    Path moduleRoot = Paths.get(
      getClass().getClassLoader().getResource(".").toURI())
        .resolve("../../../../..")
        .normalize();
    Path target = moduleRoot.resolve("docs/readme/activityLog_showcase.html");

    Files.writeString(target, html);
    System.out.println("Written to: " + target);
  }
}
