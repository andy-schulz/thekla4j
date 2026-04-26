package com.teststeps.thekla4j.allure.junit5.extensions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import com.teststeps.thekla4j.activityLog.ActivityLogEntry;
import com.teststeps.thekla4j.activityLog.ActivityLogEntryType;
import com.teststeps.thekla4j.activityLog.ActivityStatus;
import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.AllureActor;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.test.AllureResultsWriterStub;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

/**
 * Integration tests that run fixture classes through the full JUnit 5 launcher
 * and verify that {@link Thekla4jAllureJunit5Extension} correctly maps
 * {@link ActivityLogNode} trees to Allure steps via {@link AllureActor}-annotated fields.
 */
class ActivityLogStepsIntegrationTest {

  // ===== Fixtures =====

  /**
   * Fixture: single Actor with a manually injected activity log tree.
   * Uses the TheklaActivityLog API to add entries so they persist in getLogTree().
   */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  static class SingleActorFixture {
    @AllureActor
    Actor actor = Actor.named("Alice");

    @Test
    void testWithActivityLog() {
      ActivityLogEntry entry = actor.activityLog.addActivityLogEntry(
        "ClickButton", "click the submit button",
        ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
      entry.setInput("button#submit");
      entry.setOutput("clicked");
      actor.activityLog.reset(entry);
    }
  }

  /**
   * Fixture: Actor field without @AllureActor — should NOT produce steps.
   */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  static class NoAnnotationFixture {
    Actor actor = Actor.named("Bob");

    @Test
    void testWithoutAnnotation() {
      ActivityLogEntry entry = actor.activityLog.addActivityLogEntry(
        "Ignored", null,
        ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
      actor.activityLog.reset(entry);
    }
  }

  /**
   * Fixture: multiple Actors.
   */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  static class MultipleActorsFixture {
    @AllureActor
    Actor alice = Actor.named("Alice");

    @AllureActor
    Actor bob = Actor.named("Bob");

    @Test
    void testWithMultipleActors() {
      ActivityLogEntry aliceEntry = alice.activityLog.addActivityLogEntry(
        "AliceAction", null,
        ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
      alice.activityLog.reset(aliceEntry);

      ActivityLogEntry bobEntry = bob.activityLog.addActivityLogEntry(
        "BobAction", null,
        ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
      bob.activityLog.reset(bobEntry);
    }
  }

  /**
   * Fixture: Actor with attachments on root node.
   */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  static class RootAttachmentsFixture {
    @AllureActor
    Actor actor = Actor.named("Charlie");

    @Test
    void testWithRootAttachments() {
      actor.activityLog.appendAttachmentsToRootNode(
        new LogAttachment("ability-dump", "browser=chrome", LogAttachmentType.TEXT_PLAIN));
      ActivityLogEntry entry = actor.activityLog.addActivityLogEntry(
        "Navigate", null,
        ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
      actor.activityLog.reset(entry);
    }
  }

  /**
   * Fixture: Actor with video attachments.
   */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  static class VideoAttachmentsFixture {
    @AllureActor
    Actor actor = Actor.named("Dave");

    @Test
    void testWithVideoAttachments() {
      actor.activityLog.appendVideoAttachmentToRootNode(
        new LogAttachment("recording", "https://example.com/video.mp4", LogAttachmentType.VIDEO_MP4));
      ActivityLogEntry entry = actor.activityLog.addActivityLogEntry(
        "PerformAction", null,
        ActivityLogEntryType.Interaction, TASK_LOG.FULL_LOG, ActivityStatus.passed);
      actor.activityLog.reset(entry);
    }
  }

  // ===== Helpers =====

  private List<TestResult> runFixture(final Class<?> fixtureClass) {
    AllureResultsWriterStub stub = new AllureResultsWriterStub();
    AllureLifecycle testLifecycle = new AllureLifecycle(stub);
    AllureLifecycle original = Allure.getLifecycle();
    Allure.setLifecycle(testLifecycle);
    try {
      LauncherDiscoveryRequest request =
          LauncherDiscoveryRequestBuilder.request()
              .selectors(selectClass(fixtureClass))
              .build();
      Launcher launcher = LauncherFactory.create();
      launcher.execute(request);
    } finally {
      Allure.setLifecycle(original);
    }
    return stub.getTestResults();
  }

  // ===== Tests =====

  @Test
  void singleActor_activityLogMappedAsSteps() {
    List<TestResult> results = runFixture(SingleActorFixture.class);

    assertThat(results, hasSize(1));
    TestResult result = results.get(0);

    // Activity Log section with actor nested inside
    assertThat(result.getSteps(), hasSize(1));
    StepResult section = result.getSteps().get(0);
    assertThat(section.getName(), is("\uD83D\uDCCB Activity Log"));
    assertThat(section.getSteps(), hasSize(1));
    StepResult actorStep = section.getSteps().get(0);
    assertThat(actorStep.getName(), is("Log of Alice"));
    assertThat(actorStep.getStatus(), is(Status.PASSED));

    // Child activity step
    assertThat(actorStep.getSteps(), hasSize(1));
    StepResult childStep = actorStep.getSteps().get(0);
    assertThat(childStep.getName(), is("ClickButton - click the submit button"));
    assertThat(childStep.getStatus(), is(Status.PASSED));

    // Parameters: input and output
    assertThat(childStep.getParameters(), hasSize(2));
    assertThat(childStep.getParameters().get(0).getName(), is("input"));
    assertThat(childStep.getParameters().get(0).getValue(), is("button#submit"));
    assertThat(childStep.getParameters().get(1).getName(), is("output"));
    assertThat(childStep.getParameters().get(1).getValue(), is("clicked"));
  }

  @Test
  void noAllureActorAnnotation_noStepsMapped() {
    List<TestResult> results = runFixture(NoAnnotationFixture.class);

    assertThat(results, hasSize(1));
    // No steps should be added since field is not annotated
    assertThat(results.get(0).getSteps(), hasSize(0));
  }

  @Test
  void multipleActors_eachWrappedInOwnStep() {
    List<TestResult> results = runFixture(MultipleActorsFixture.class);

    assertThat(results, hasSize(1));
    TestResult result = results.get(0);

    // Activity Log section with two actor steps nested inside
    assertThat(result.getSteps(), hasSize(1));
    StepResult section = result.getSteps().get(0);
    assertThat(section.getSteps(), hasSize(2));

    // Verify both actors present (order may vary by field declaration order)
    boolean hasAlice = section.getSteps().stream().anyMatch(s -> "Log of Alice".equals(s.getName()));
    boolean hasBob = section.getSteps().stream().anyMatch(s -> "Log of Bob".equals(s.getName()));
    assertThat("Alice step present", hasAlice, is(true));
    assertThat("Bob step present", hasBob, is(true));
  }

  @Test
  void rootAttachments_emittedOnActorWrapperStep() {
    List<TestResult> results = runFixture(RootAttachmentsFixture.class);

    assertThat(results, hasSize(1));
    StepResult actorStep = results.get(0).getSteps().get(0).getSteps().get(0);

    // Root-level attachment should be on the actor wrapper step
    assertThat(actorStep.getAttachments(), hasSize(1));
    assertThat(actorStep.getAttachments().get(0).getName(), is("ability-dump"));
  }

  @Test
  void videoAttachments_emittedOnActorWrapperStep() {
    List<TestResult> results = runFixture(VideoAttachmentsFixture.class);

    assertThat(results, hasSize(1));
    StepResult actorStep = results.get(0).getSteps().get(0).getSteps().get(0);

    // Video attachment should be emitted
    assertThat(actorStep.getAttachments(), hasSize(1));
    assertThat(actorStep.getAttachments().get(0).getName(), is("recording"));
  }
}
