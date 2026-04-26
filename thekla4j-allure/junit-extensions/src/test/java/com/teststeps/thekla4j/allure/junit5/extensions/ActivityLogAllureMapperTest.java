package com.teststeps.thekla4j.allure.junit5.extensions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.teststeps.thekla4j.activityLog.ActivityStatus;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.allure.shared.ActivityLogAllureMapper;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.test.AllureResultsWriterStub;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ActivityLogAllureMapper}.
 *
 * <p>Constructs {@link ActivityLogNode} trees directly (no Actor required)
 * and verifies the resulting Allure step hierarchy via {@link AllureResultsWriterStub}.
 */
class ActivityLogAllureMapperTest {

  private AllureResultsWriterStub writer;
  private AllureLifecycle lifecycle;

  @BeforeEach
  void setUp() {
    writer = new AllureResultsWriterStub();
    lifecycle = new AllureLifecycle(writer);
  }

  // ===== Status mapping =====

  @Test
  void mapStatus_passed_returnsPASSED() {
    assertThat(ActivityLogAllureMapper.mapStatus(ActivityStatus.passed), is(Status.PASSED));
  }

  @Test
  void mapStatus_failed_returnsFAILED() {
    assertThat(ActivityLogAllureMapper.mapStatus(ActivityStatus.failed), is(Status.FAILED));
  }

  @Test
  void mapStatus_running_returnsBROKEN() {
    assertThat(ActivityLogAllureMapper.mapStatus(ActivityStatus.running), is(Status.BROKEN));
  }

  @Test
  void mapStatus_null_returnsBROKEN() {
    assertThat(ActivityLogAllureMapper.mapStatus(null), is(Status.BROKEN));
  }

  @Test
  void aggregateWorstStatus_failedWinsOverBroken() {
    final Status actual = ActivityLogAllureMapper.aggregateWorstStatus(Status.FAILED, Status.BROKEN);
    assertThat(actual, is(Status.FAILED));
  }

  @Test
  void aggregateWorstStatus_failedWinsWhenComparedAgainstPassed() {
    final Status actual = ActivityLogAllureMapper.aggregateWorstStatus(Status.PASSED, Status.FAILED);
    assertThat(actual, is(Status.FAILED));
  }

  @Test
  void aggregateWorstStatus_brokenWinsOverPassed() {
    final Status actual = ActivityLogAllureMapper.aggregateWorstStatus(Status.PASSED, Status.BROKEN);
    assertThat(actual, is(Status.BROKEN));
  }

  // ===== Timestamp parsing =====

  @Test
  void parseTimestamp_validString_returnsEpochMillis() {
    final Long millis = ActivityLogAllureMapper.parseTimestamp("2026-01-15 10:30:00.000000");
    assertThat(millis, is(notNullValue()));
    assertThat(millis > 0, is(true));
  }

  @Test
  void parseTimestamp_null_returnsFallback() {
    final Long millis = ActivityLogAllureMapper.parseTimestamp(null);
    assertThat(millis, is(notNullValue()));
  }

  @Test
  void parseTimestamp_invalid_returnsFallback() {
    final Long millis = ActivityLogAllureMapper.parseTimestamp("not-a-timestamp");
    assertThat(millis, is(notNullValue()));
  }

  // ===== Tree mapping =====

  @Test
  void singleChildNode_mappedAsNestedStep() {
    final ActivityLogNode child = node("ClickButton", ActivityStatus.passed, null, null);
    final ActivityLogNode root = node("START", ActivityStatus.passed,
      List.of(child), null);

    final TestResult testResult = runMapper("TestActor", root);

    // Actor wrapper step
    assertThat(testResult.getSteps(), hasSize(1));
    final StepResult actorStep = testResult.getSteps().get(0);
    assertThat(actorStep.getName(), is("Log of TestActor"));
    assertThat(actorStep.getStatus(), is(Status.PASSED));

    // Child step
    assertThat(actorStep.getSteps(), hasSize(1));
    assertThat(actorStep.getSteps().get(0).getName(), is("ClickButton"));
    assertThat(actorStep.getSteps().get(0).getStatus(), is(Status.PASSED));
  }

  @Test
  void nestedChildren_mappedRecursively() {
    final ActivityLogNode grandchild = node("TypeText", ActivityStatus.passed, null, null);
    final ActivityLogNode child = node("FillForm", ActivityStatus.passed,
      List.of(grandchild), null);
    final ActivityLogNode root = node("START", ActivityStatus.passed,
      List.of(child), null);

    final TestResult testResult = runMapper("Actor1", root);

    final StepResult actorStep = testResult.getSteps().get(0);
    assertThat(actorStep.getSteps(), hasSize(1));
    final StepResult fillForm = actorStep.getSteps().get(0);
    assertThat(fillForm.getSteps(), hasSize(1));
    assertThat(fillForm.getSteps().get(0).getName(), is("TypeText"));
  }

  @Test
  void failedChild_mappedWithFailedStatus() {
    final ActivityLogNode child = node("Navigate", ActivityStatus.failed, null, null);
    final ActivityLogNode root = node("START", ActivityStatus.failed,
      List.of(child), null);

    final TestResult testResult = runMapper("Actor1", root);

    final StepResult actorStep = testResult.getSteps().get(0);
    assertThat(actorStep.getStatus(), is(Status.FAILED));
    assertThat(actorStep.getSteps().get(0).getStatus(), is(Status.FAILED));
  }

  @Test
  void inputAndOutput_mappedAsParameters() {
    final ActivityLogNode child = nodeWithIO("SendRequest", "GET /api", "200 OK");
    final ActivityLogNode root = node("START", ActivityStatus.passed,
      List.of(child), null);

    final TestResult testResult = runMapper("Actor1", root);

    final StepResult step = testResult.getSteps().get(0).getSteps().get(0);
    assertThat(step.getParameters(), hasSize(2));
    assertThat(step.getParameters().get(0).getName(), is("input"));
    assertThat(step.getParameters().get(0).getValue(), is("GET /api"));
    assertThat(step.getParameters().get(1).getName(), is("output"));
    assertThat(step.getParameters().get(1).getValue(), is("200 OK"));
  }

  @Test
  void blankInputOutput_notAddedAsParameters() {
    final ActivityLogNode child = nodeWithIO("SimpleAction", "", "  ");
    final ActivityLogNode root = node("START", ActivityStatus.passed,
      List.of(child), null);

    final TestResult testResult = runMapper("Actor1", root);

    final StepResult step = testResult.getSteps().get(0).getSteps().get(0);
    assertThat(step.getParameters(), is(empty()));
  }

  @Test
  void nullRoot_producesNoSteps() {
    final TestResult testResult = runMapper("Actor1", null);
    assertThat(testResult.getSteps(), is(empty()));
  }

  @Test
  void emptyRoot_producesActorStepWithNoChildren() {
    final ActivityLogNode root = node("START", ActivityStatus.passed, null, null);

    final TestResult testResult = runMapper("Actor1", root);

    assertThat(testResult.getSteps(), hasSize(1));
    assertThat(testResult.getSteps().get(0).getSteps(), is(empty()));
  }

  @Test
  void rootAttachments_emittedOnActorWrapperStep() {
    final NodeAttachment attachment = new LogAttachment(
                                                        "screenshot", "image data here", LogAttachmentType.TEXT_PLAIN);
    final ActivityLogNode root = node("START", ActivityStatus.passed, null,
      List.of(attachment));

    final TestResult testResult = runMapper("Actor1", root);

    // The actor wrapper step should have the attachment
    final StepResult actorStep = testResult.getSteps().get(0);
    assertThat(actorStep.getAttachments(), hasSize(1));
    assertThat(actorStep.getAttachments().get(0).getName(), is("screenshot"));
  }

  @Test
  void childAttachments_emittedOnChildStep() {
    final NodeAttachment attachment = new LogAttachment(
                                                        "response", "body text", LogAttachmentType.TEXT_PLAIN);
    final ActivityLogNode child = new ActivityLogNode(
                                                      "GetResponse", null,
                                                      "2026-01-15 10:30:00.000000", "2026-01-15 10:30:01.000000",
                                                      Duration.ofSeconds(1), "", "",
                                                      List.of(attachment), null, null, ActivityStatus.passed, null);
    final ActivityLogNode root = node("START", ActivityStatus.passed,
      List.of(child), null);

    final TestResult testResult = runMapper("Actor1", root);

    final StepResult childStep = testResult.getSteps().get(0).getSteps().get(0);
    assertThat(childStep.getAttachments(), hasSize(1));
    assertThat(childStep.getAttachments().get(0).getName(), is("response"));
  }

  @Test
  void videoAttachments_emittedAsUriList() {
    final NodeAttachment videoAttachment = new LogAttachment(
                                                             "test-video", "https://example.com/video.mp4", LogAttachmentType.VIDEO_MP4);
    final ActivityLogNode root = new ActivityLogNode(
                                                     "START", null,
                                                     "2026-01-15 10:30:00.000000", "2026-01-15 10:30:01.000000",
                                                     Duration.ofSeconds(1), "", "",
                                                     null, List.of(videoAttachment), null, ActivityStatus.passed, null);

    final TestResult testResult = runMapper("Actor1", root);

    final StepResult actorStep = testResult.getSteps().get(0);
    assertThat(actorStep.getAttachments(), hasSize(1));
    assertThat(actorStep.getAttachments().get(0).getName(), is("test-video"));
  }

  // ===== Helpers =====

  private TestResult runMapper(final String actorName, final ActivityLogNode root) {
    final String testUuid = UUID.randomUUID().toString();
    final TestResult testResult = new TestResult().setUuid(testUuid);
    lifecycle.scheduleTestCase(testResult);
    lifecycle.startTestCase(testUuid);

    ActivityLogAllureMapper.mapActivityLogToAllureSteps(lifecycle, testUuid, actorName, root);

    lifecycle.stopTestCase(testUuid);
    lifecycle.writeTestCase(testUuid);

    final List<TestResult> results = writer.getTestResults();
    assertThat(results, hasSize(1));
    return results.get(0);
  }

  private ActivityLogNode node(final String name, final ActivityStatus status, final List<ActivityLogNode> children, final List<NodeAttachment> attachments) {
    return new ActivityLogNode(
                               name, null,
                               "2026-01-15 10:30:00.000000", "2026-01-15 10:30:01.000000",
                               Duration.ofSeconds(1), "", "",
                               attachments, null, null, status, children);
  }

  private ActivityLogNode nodeWithIO(final String name, final String input, final String output) {
    return new ActivityLogNode(
                               name, null,
                               "2026-01-15 10:30:00.000000", "2026-01-15 10:30:01.000000",
                               Duration.ofSeconds(1), input, output,
                               null, null, null, ActivityStatus.passed, null);
  }
}
