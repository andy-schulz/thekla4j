package com.teststeps.thekla4j.allure.shared;

import static com.teststeps.thekla4j.allure.shared.ActivityLogAllureMapper.MAX_PARAMETER_VALUE_LENGTH_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import com.teststeps.thekla4j.activityLog.ActivityLogEntryType;
import com.teststeps.thekla4j.activityLog.ActivityStatus;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Attachment;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.test.AllureResultsWriterStub;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The parameter values of a step are written into the Allure result file. An unbounded value makes that file
 * unparseable for the report generator, so a long value is capped and attached instead.
 */
class TestParameterValueCap {

  private static final int DEFAULT_LIMIT = 32768;

  private AllureResultsWriterStub stub;

  @AfterEach
  void clearProperty() {
    System.clearProperty(MAX_PARAMETER_VALUE_LENGTH_PROPERTY);
  }

  /**
   * maps a single activity node carrying the given output and returns the step it produced
   */
  private StepResult mapNodeWithOutput(final String output) {
    stub = new AllureResultsWriterStub();
    final AllureLifecycle lifecycle = new AllureLifecycle(stub);

    final String testUuid = UUID.randomUUID().toString();
    lifecycle.scheduleTestCase(new TestResult().setUuid(testUuid).setName("parameter cap"));
    lifecycle.startTestCase(testUuid);

    final String sectionUuid = ActivityLogAllureMapper.openActivityLogSection(lifecycle, testUuid);
    ActivityLogAllureMapper.mapActivityLogToAllureSteps(lifecycle, sectionUuid, "Tester", rootWithOutput(output));
    ActivityLogAllureMapper.closeActivityLogSection(lifecycle, sectionUuid, Status.PASSED);

    lifecycle.stopTestCase(testUuid);
    lifecycle.writeTestCase(testUuid);

    return activityStep();
  }

  private static ActivityLogNode rootWithOutput(final String output) {
    final ActivityLogNode activity = node("readUsers", output, List.of());
    return node("START", "", List.of(activity));
  }

  private static ActivityLogNode node(final String name, final String output, final List<ActivityLogNode> children) {
    return new ActivityLogNode(
                               name, "", "2026-10-07 08:00:00.000000", "2026-10-07 08:00:01.000000", Duration.ofSeconds(1),
                               "", output, List.of(), List.of(), ActivityLogEntryType.Task, ActivityStatus.passed, children);
  }

  /**
   * section step -&gt; actor step -&gt; the single activity step
   */
  private StepResult activityStep() {
    final TestResult result = stub.getTestResults().get(0);
    return result.getSteps().get(0).getSteps().get(0).getSteps().get(0);
  }

  private static Optional<Parameter> parameter(final StepResult step, final String name) {
    return step.getParameters().stream().filter(p -> name.equals(p.getName())).findFirst();
  }

  @Test
  @DisplayName("a value over the cap is truncated and the full value is attached")
  void oversizedValueIsTruncatedAndAttached() {
    final String original = "u".repeat(40_000_000);

    final StepResult step = mapNodeWithOutput(original);
    final String stored = parameter(step, "output").orElseThrow().getValue();

    assertThat("the stored value is capped", stored.length(), lessThanOrEqualTo(DEFAULT_LIMIT + 200));
    assertThat("the marker names the shown length", stored, containsString(String.valueOf(DEFAULT_LIMIT)));
    assertThat("the marker names the original length", stored, containsString(String.valueOf(original.length())));
    assertThat("the marker names the attachment", stored, containsString("output (full)"));

    final List<Attachment> attachments = step.getAttachments();
    assertThat("exactly one attachment is emitted", attachments, hasSize(1));
    assertThat("it is named after the parameter", attachments.get(0).getName(), equalTo("output (full)"));
    assertThat("it is previewable text", attachments.get(0).getType(), equalTo("text/plain"));

    final byte[] attached = stub.getAttachments().get(attachments.get(0).getSource());
    assertThat("it carries the complete original value",
      new String(attached, StandardCharsets.UTF_8).length(), equalTo(original.length()));
  }

  @Test
  @DisplayName("a short value is written unchanged and emits no attachment")
  void shortValueIsUntouched() {
    final String original = "a".repeat(1_000);

    final StepResult step = mapNodeWithOutput(original);

    assertThat("the value is passed through", parameter(step, "output").orElseThrow().getValue(), equalTo(original));
    assertThat("no attachment is emitted", step.getAttachments(), empty());
  }

  @Test
  @DisplayName("a value of exactly the cap is not truncated")
  void valueOfExactlyTheLimitIsUntouched() {
    final String original = "a".repeat(DEFAULT_LIMIT);

    final StepResult step = mapNodeWithOutput(original);

    assertThat("the value is passed through", parameter(step, "output").orElseThrow().getValue(), equalTo(original));
    assertThat("no attachment is emitted", step.getAttachments(), empty());
  }

  @Test
  @DisplayName("setting the cap to zero restores the uncapped behaviour")
  void capCanBeDisabled() {
    System.setProperty(MAX_PARAMETER_VALUE_LENGTH_PROPERTY, "0");
    final String original = "u".repeat(DEFAULT_LIMIT * 2);

    final StepResult step = mapNodeWithOutput(original);

    assertThat("the value is not truncated", parameter(step, "output").orElseThrow().getValue(), equalTo(original));
    assertThat("no attachment is emitted", step.getAttachments(), empty());
  }

  @Test
  @DisplayName("an unparseable cap falls back to the default instead of throwing")
  void unparseableCapFallsBackToTheDefault() {
    System.setProperty(MAX_PARAMETER_VALUE_LENGTH_PROPERTY, "not a number");
    final String original = "a".repeat(DEFAULT_LIMIT + 10);

    final StepResult step = mapNodeWithOutput(original);

    assertThat("the default cap is applied", parameter(step, "output").orElseThrow().getValue(),
      containsString("truncated"));
  }

  @Test
  @DisplayName("truncating never splits a surrogate pair")
  void surrogatePairIsNotSplit() {
    System.setProperty(MAX_PARAMETER_VALUE_LENGTH_PROPERTY, "11");
    // every emoji is two chars, so a cap of 11 lands in the middle of the sixth pair
    final String original = "😀".repeat(20);

    final StepResult step = mapNodeWithOutput(original);
    final String stored = parameter(step, "output").orElseThrow().getValue();
    final String prefix = stored.substring(0, stored.indexOf("\n[thekla4j:"));

    assertThat("the kept prefix stops before the pair", prefix.length(), equalTo(10));
    assertThat("the prefix is valid UTF-16 and round trips",
      new String(prefix.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8), equalTo(prefix));
    assertThat("no replacement character is introduced", prefix.contains("�"), equalTo(false));
  }

  @Test
  @DisplayName("the attachment lands on the step the parameter belongs to, not on its parent")
  void attachmentLandsOnTheOwningStep() {
    final String original = "u".repeat(DEFAULT_LIMIT * 2);

    final StepResult step = mapNodeWithOutput(original);
    final TestResult result = stub.getTestResults().get(0);

    assertThat("the activity step owns the attachment", step.getAttachments(), hasSize(1));
    assertThat("the activity log section does not", result.getSteps().get(0).getAttachments(), empty());
    assertThat("the actor step does not", result.getSteps().get(0).getSteps().get(0).getAttachments(), empty());
    assertThat("the test case does not", result.getAttachments(), empty());
  }
}
