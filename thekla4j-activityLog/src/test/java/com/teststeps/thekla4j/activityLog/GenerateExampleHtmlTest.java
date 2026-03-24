package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import org.junit.jupiter.api.Test;

/**
 * Generates docs/readme/activityLog.html — a self-contained example showing every
 * feature of the ActivityLog HTML report. Run manually when the report format changes.
 */
class GenerateExampleHtmlTest {

  // ── helpers ──────────────────────────────────────────────────────────────────

  private static ActivityLogNode interaction(String name, String desc, String started, String ended, Duration dur, String input, String output, java.util.List<NodeAttachment> attachments, ActivityStatus status) {
    return new ActivityLogNode(name, desc, started, ended, dur,
                               input, output, attachments, null,
                               ActivityLogEntryType.Interaction, status,
                               List.<ActivityLogNode>empty().toJavaList());
  }

  private static ActivityLogNode task(String name, String desc, String started, String ended, Duration dur, String input, String output, java.util.List<NodeAttachment> attachments, ActivityStatus status, java.util.List<ActivityLogNode> children) {
    return new ActivityLogNode(name, desc, started, ended, dur,
                               input, output, attachments, null,
                               ActivityLogEntryType.Task, status, children);
  }

  private static ActivityLogNode group(String name, String desc, String started, String ended, Duration dur, ActivityStatus status, java.util.List<ActivityLogNode> children) {
    return new ActivityLogNode(name, desc, started, ended, dur,
                               "", "", null, null,
                               ActivityLogEntryType.Group, status, children);
  }

  private static String redDotBase64() throws IOException {
    try (InputStream in = GenerateExampleHtmlTest.class
        .getClassLoader()
        .getResourceAsStream("reddot.png")) {
      if (in == null) return "";
      return Base64.getEncoder().encodeToString(in.readAllBytes());
    }
  }

  // ── test / generator ─────────────────────────────────────────────────────────

  @Test
  void generateExampleHtml() throws IOException, URISyntaxException {

    String redDot = redDotBase64();

    // ── Interactions: Login flow ─────────────────────────────────────────────
    ActivityLogNode navigateToLogin = interaction(
      "Navigate", "navigate to the login page",
      "2026-03-24T08:00:00.100", "2026-03-24T08:00:00.450",
      Duration.ofMillis(350),
      "https://app.example.com/login", "200 OK",
      List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.passed);

    ActivityLogNode fillUsername = interaction(
      "Fill", "fill username field with {value}",
      "2026-03-24T08:00:00.500", "2026-03-24T08:00:00.620",
      Duration.ofMillis(120),
      "john.doe@example.com", "",
      List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.passed);

    ActivityLogNode fillPassword = interaction(
      "Fill", "fill password field with {value}",
      "2026-03-24T08:00:00.630", "2026-03-24T08:00:00.710",
      Duration.ofMillis(80),
      "••••••••", "",
      List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.passed);

    ActivityLogNode fillFormTask = task(
      "FillLoginForm", "fill the login form",
      "2026-03-24T08:00:00.490", "2026-03-24T08:00:00.720",
      Duration.ofMillis(230),
      "", "", List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.passed,
      List.of(fillUsername, fillPassword).toJavaList());

    ActivityLogNode clickLogin = interaction(
      "Click", "click the login button",
      "2026-03-24T08:00:00.730", "2026-03-24T08:00:01.100",
      Duration.ofMillis(370),
      "", "Redirected to /dashboard",
      List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.passed);

    ActivityLogNode loginFlow = task(
      "Login", "user logs in with valid credentials",
      "2026-03-24T08:00:00.100", "2026-03-24T08:00:01.100",
      Duration.ofMillis(1000),
      "", "", List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.passed,
      List.of(navigateToLogin, fillFormTask, clickLogin).toJavaList());

    // ── Interactions: Search + failure ───────────────────────────────────────
    String longDesc = "search for the product 'Wireless Noise-Cancelling Headphones' in the " +
        "global search bar and verify that at least one result is returned matching the query";

    ActivityLogNode enterQuery = interaction(
      "Fill", "enter search query into the search field",
      "2026-03-24T08:00:01.200", "2026-03-24T08:00:01.380",
      Duration.ofMillis(180),
      "Wireless Noise-Cancelling Headphones", "",
      List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.passed);

    StacktraceAttachment consoleError = StacktraceAttachment.of(
      LocalDateTime.of(2026, 3, 24, 8, 0, 1, 600_000_000),
      "console-error", "ERROR", "javascript",
      "Uncaught TypeError: Cannot read property 'length' of undefined",
      List.of(
        new LogStackFrameAttachment("processResults", "search.bundle.js", 142, 18),
        new LogStackFrameAttachment("onSearchResponse", "search.bundle.js", 89, 5),
        new LogStackFrameAttachment("handleEvent", "event-handler.js", 33, 12)));

    NodeAttachment errorLog = new LogAttachment(
                                                "error-details",
                                                "AssertionError: expected 5 results but got 0\n" + "  Expected: result count >= 1\n" +
                                                    "  Actual:   result count  = 0\n" + "  Query:    'Wireless Noise-Cancelling Headphones'",
                                                LogAttachmentType.TEXT_PLAIN);

    ActivityLogNode verifyResults = interaction(
      "Verify", longDesc,
      "2026-03-24T08:00:01.400", "2026-03-24T08:00:01.650",
      Duration.ofMillis(250),
      "expectedResultCount: 1",
      "AssertionError: expected 5 results but got 0",
      List.of(errorLog, (NodeAttachment) consoleError).toJavaList(),
      ActivityStatus.failed);

    ActivityLogNode screenshotStep = interaction(
      "TakeScreenshot", "capture screenshot on failure",
      "2026-03-24T08:00:01.660", "2026-03-24T08:00:01.820",
      Duration.ofMillis(160),
      "", "",
      List.of((NodeAttachment) new LogAttachment("failure-screenshot", redDot, LogAttachmentType.IMAGE_BASE64))
          .toJavaList(),
      ActivityStatus.passed);

    ActivityLogNode searchFlow = task(
      "SearchProduct", longDesc,
      "2026-03-24T08:00:01.200", "2026-03-24T08:00:01.820",
      Duration.ofMillis(620),
      "", "", List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.failed,
      List.of(enterQuery, verifyResults, screenshotStep).toJavaList());

    // ── Group: parallel assertions ───────────────────────────────────────────
    ActivityLogNode checkTitle = interaction(
      "Assert", "assert page title equals 'Dashboard | MyApp'",
      "2026-03-24T08:00:01.900", "2026-03-24T08:00:01.960",
      Duration.ofMillis(60),
      "Dashboard | MyApp", "Dashboard | MyApp",
      List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.passed);

    ActivityLogNode checkUrl = interaction(
      "Assert", "assert current URL contains '/dashboard'",
      "2026-03-24T08:00:01.900", "2026-03-24T08:00:01.950",
      Duration.ofMillis(50),
      "/dashboard", "https://app.example.com/dashboard",
      List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.passed);

    ActivityLogNode parallelGroup = group(
      "VerifyDashboard", "verify dashboard state",
      "2026-03-24T08:00:01.900", "2026-03-24T08:00:01.960",
      Duration.ofMillis(60),
      ActivityStatus.passed,
      List.of(checkTitle, checkUrl).toJavaList());

    // ── Running step ─────────────────────────────────────────────────────────
    ActivityLogNode uploading = interaction(
      "Upload", "upload test artefacts to report server",
      "2026-03-24T08:00:02.100", "not finished",
      Duration.ZERO,
      "report.zip", "",
      List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.running);

    ActivityLogNode uploadTask = task(
      "UploadReport", "upload the generated test report",
      "2026-03-24T08:00:02.050", "not finished",
      Duration.ZERO,
      "", "", List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.running,
      List.of(uploading).toJavaList());

    // ── Root ─────────────────────────────────────────────────────────────────
    ActivityLogNode root = task(
      "START", "TestActor attempts to run the full regression suite",
      "2026-03-24T08:00:00.000", "not finished",
      Duration.ofMillis(2200),
      "", "",
      List.<NodeAttachment>empty().toJavaList(),
      ActivityStatus.failed,
      List.of(loginFlow, searchFlow, parallelGroup, uploadTask).toJavaList());

    // ── Generate & write ─────────────────────────────────────────────────────
    String fragment = LogFormatter.formatLogAsHtmlTree(root);

    String html = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>ActivityLog Example</title>
        </head>
        <body>
        """ + fragment + """
        </body>
        </html>
        """;

    Path target = Paths.get("../../docs/readme/activityLog.html")
        .toAbsolutePath()
        .normalize();
    // Resolve relative to module root
    Path moduleRoot = Paths.get(
      GenerateExampleHtmlTest.class.getClassLoader()
          .getResource(".")
          .toURI())
        .resolve("../../../../..")
        .normalize();
    target = moduleRoot.resolve("docs/readme/activityLog.html");

    Files.writeString(target, html);
    System.out.println("Written to: " + target);
  }
}
