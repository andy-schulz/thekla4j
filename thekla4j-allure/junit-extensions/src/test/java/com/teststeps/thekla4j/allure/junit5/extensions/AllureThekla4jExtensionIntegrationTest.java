package com.teststeps.thekla4j.allure.junit5.extensions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import com.teststeps.thekla4j.allure.junit5.extensions.tags.Epic;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Feature;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Issues;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.ParentSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Reqs;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Story;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.SubSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Suite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.TestId;
import com.teststeps.thekla4j.commons.error.ActivityError;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.test.AllureResultsWriterStub;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

/**
 * Integration tests that run fixture classes through the full JUnit 5 launcher stack (including
 * {@code AllureJunitPlatform}) and verify that {@link Thekla4jAllureJunit5Extension} correctly records
 * both label annotations and ActivityError → FAILED status.
 */
class AllureThekla4jExtensionIntegrationTest {

  // ===== Fixture classes =====

  /** Fixture: ActivityError should be reported as FAILED, not BROKEN. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  static class ActivityErrorFixture {
    @Test
    void failingTest() throws ActivityError {
      throw ActivityError.of("expected condition was not met");
    }
  }

  /** Fixture: class-level annotations covering all six label types. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  @Epic("TestEpic")
  @Feature("TestFeature")
  @Story("TestStory")
  @Suite("TestSuite")
  @SubSuite("TestSubSuite")
  @ParentSuite("TestParentSuite")
  static class ClassAnnotationsFixture {
    @Test
    void passingTest() {
    }
  }

  /** Fixture: method-level annotations override class-level ones. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  @Epic("ClassEpic")
  @Feature("ClassFeature")
  @Story("ClassStory")
  static class MethodOverridesFixture {
    @Epic("MethodEpic")
    @Feature("MethodFeature")
    @Story("MethodStory")
    @Test
    void annotatedTest() {
    }
  }

  /** Fixture: class-level issue links. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  @Issues({"CLASS-1", "CLASS-2"})
  static class ClassIssuesFixture {
    @Test
    void passingTest() {
    }
  }

  /** Fixture: method-level issue links override class-level links. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  @Issues({"CLASS-1", "CLASS-2"})
  static class MethodIssuesOverrideFixture {
    @Issues({"METHOD-1", "METHOD-2"})
    @Test
    void annotatedTest() {
    }
  }

  /** Fixture: no annotations. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  static class NoAnnotationsFixture {
    @Test
    void passingTest() {
    }
  }

  /** Fixture: class-level requirement links. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  @Reqs({"REQ-1", "REQ-2"})
  static class ClassReqsFixture {
    @Test
    void passingTest() {
    }
  }

  /** Fixture: method-level requirement links override class-level links. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  @Reqs({"REQ-1", "REQ-2"})
  static class MethodReqsOverrideFixture {
    @Reqs({"METHOD-REQ-1", "METHOD-REQ-2"})
    @Test
    void annotatedTest() {
    }
  }

  /** Fixture: method-level @TestId. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  static class MethodTestIdFixture {
    @TestId("TC-001")
    @Test
    void annotatedTest() {
    }
  }

  /** Fixture: class-level @TestId. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  @TestId("CLASS-TC")
  static class ClassTestIdFixture {
    @Test
    void passingTest() {
    }
  }

  /** Fixture: method-level @TestId overrides class-level. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  @TestId("CLASS-TC")
  static class MethodTestIdOverrideFixture {
    @TestId("METHOD-TC")
    @Test
    void annotatedTest() {
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

  private String labelValue(final TestResult result, final String name) {
    return result.getLabels()
        .stream()
        .filter(l -> name.equals(l.getName()))
        .map(Label::getValue)
        .findFirst()
        .orElse(null);
  }

  private List<Label> labelsNamed(final TestResult result, final String name) {
    return result.getLabels()
        .stream()
        .filter(l -> name.equals(l.getName()))
        .collect(Collectors.toList());
  }

  private List<String> issueLinkNames(final TestResult result) {
    return result.getLinks()
        .stream()
        .filter(link -> "issue".equalsIgnoreCase(link.getType()))
        .map(link -> link.getName())
        .collect(Collectors.toList());
  }

  private List<String> requirementLinkNames(final TestResult result) {
    return result.getLinks()
        .stream()
        .filter(link -> "requirement".equalsIgnoreCase(link.getType()))
        .map(link -> link.getName())
        .collect(Collectors.toList());
  }

  // ===== ActivityError tests =====

  @Test
  void activityError_in_real_junit5_run_isReportedAsFailedInAllure() {
    List<TestResult> results = runFixture(ActivityErrorFixture.class);

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getStatus(), is(Status.FAILED));
  }

  // ===== Label annotation tests =====

  @Test
  void classAnnotations_epic_isRecordedInAllureOutput() {
    List<TestResult> results = runFixture(ClassAnnotationsFixture.class);

    assertThat(results, hasSize(1));
    assertThat(labelValue(results.get(0), "epic"), is("TestEpic"));
  }

  @Test
  void classAnnotations_feature_isRecordedInAllureOutput() {
    List<TestResult> results = runFixture(ClassAnnotationsFixture.class);

    assertThat(results, hasSize(1));
    assertThat(labelValue(results.get(0), "feature"), is("TestFeature"));
  }

  @Test
  void classAnnotations_story_isRecordedInAllureOutput() {
    List<TestResult> results = runFixture(ClassAnnotationsFixture.class);

    assertThat(results, hasSize(1));
    assertThat(labelValue(results.get(0), "story"), is("TestStory"));
  }

  @Test
  void classAnnotations_suite_replacesAutoGeneratedSuiteLabel() {
    List<TestResult> results = runFixture(ClassAnnotationsFixture.class);

    assertThat(results, hasSize(1));
    List<Label> suiteLabels = labelsNamed(results.get(0), "suite");
    assertThat(suiteLabels, hasSize(1));
    assertThat(suiteLabels.get(0).getValue(), is("TestSuite"));
  }

  @Test
  void classAnnotations_subSuite_isRecordedInAllureOutput() {
    List<TestResult> results = runFixture(ClassAnnotationsFixture.class);

    assertThat(results, hasSize(1));
    List<Label> subSuiteLabels = labelsNamed(results.get(0), "subSuite");
    assertThat(subSuiteLabels, hasSize(1));
    assertThat(subSuiteLabels.get(0).getValue(), is("TestSubSuite"));
  }

  @Test
  void classAnnotations_parentSuite_isRecordedInAllureOutput() {
    List<TestResult> results = runFixture(ClassAnnotationsFixture.class);

    assertThat(results, hasSize(1));
    List<Label> parentSuiteLabels = labelsNamed(results.get(0), "parentSuite");
    assertThat(parentSuiteLabels, hasSize(1));
    assertThat(parentSuiteLabels.get(0).getValue(), is("TestParentSuite"));
  }

  @Test
  void methodAnnotations_override_classLevelEpicInAllureOutput() {
    List<TestResult> results = runFixture(MethodOverridesFixture.class);

    assertThat(results, hasSize(1));
    List<Label> epicLabels = labelsNamed(results.get(0), "epic");
    assertThat(epicLabels, hasSize(1));
    assertThat(epicLabels.get(0).getValue(), is("MethodEpic"));
  }

  @Test
  void methodAnnotations_override_classLevelFeatureInAllureOutput() {
    List<TestResult> results = runFixture(MethodOverridesFixture.class);

    assertThat(results, hasSize(1));
    List<Label> featureLabels = labelsNamed(results.get(0), "feature");
    assertThat(featureLabels, hasSize(1));
    assertThat(featureLabels.get(0).getValue(), is("MethodFeature"));
  }

  @Test
  void methodAnnotations_override_classLevelStoryInAllureOutput() {
    List<TestResult> results = runFixture(MethodOverridesFixture.class);

    assertThat(results, hasSize(1));
    List<Label> storyLabels = labelsNamed(results.get(0), "story");
    assertThat(storyLabels, hasSize(1));
    assertThat(storyLabels.get(0).getValue(), is("MethodStory"));
  }

  @Test
  void classAnnotations_issues_areRecordedInAllureOutput() {
    List<TestResult> results = runFixture(ClassIssuesFixture.class);

    assertThat(results, hasSize(1));
    assertThat(issueLinkNames(results.get(0)), is(List.of("CLASS-1", "CLASS-2")));
  }

  @Test
  void methodAnnotations_issues_overrideClassLevelIssuesInAllureOutput() {
    List<TestResult> results = runFixture(MethodIssuesOverrideFixture.class);

    assertThat(results, hasSize(1));
    assertThat(issueLinkNames(results.get(0)), is(List.of("METHOD-1", "METHOD-2")));
  }

  @Test
  void classAnnotations_reqs_areRecordedInAllureOutput() {
    List<TestResult> results = runFixture(ClassReqsFixture.class);

    assertThat(results, hasSize(1));
    assertThat(requirementLinkNames(results.get(0)), is(List.of("REQ-1", "REQ-2")));
  }

  @Test
  void methodAnnotations_reqs_overrideClassLevelReqsInAllureOutput() {
    List<TestResult> results = runFixture(MethodReqsOverrideFixture.class);

    assertThat(results, hasSize(1));
    assertThat(requirementLinkNames(results.get(0)), is(List.of("METHOD-REQ-1", "METHOD-REQ-2")));
  }

  @Test
  void noAnnotations_allureOutputContainsNoTagLabels() {
    List<TestResult> results = runFixture(NoAnnotationsFixture.class);

    assertThat(results, hasSize(1));
    assertThat(labelsNamed(results.get(0), "epic"), hasSize(0));
    assertThat(labelsNamed(results.get(0), "feature"), hasSize(0));
    assertThat(labelsNamed(results.get(0), "story"), hasSize(0));
  }

  // ===== TestId tests =====

  @Test
  void methodTestId_appendedToNameAndSetsTestCaseId() {
    List<TestResult> results = runFixture(MethodTestIdFixture.class);

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getName(), is("annotatedTest() (TestId: TC-001)"));
    assertThat(results.get(0).getTestCaseId(), is("TC-001"));
  }

  @Test
  void classTestId_appendedToNameAndSetsTestCaseId() {
    List<TestResult> results = runFixture(ClassTestIdFixture.class);

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getName(), is("passingTest() (TestId: CLASS-TC)"));
    assertThat(results.get(0).getTestCaseId(), is("CLASS-TC"));
  }

  @Test
  void methodTestId_overridesClassLevelTestId() {
    List<TestResult> results = runFixture(MethodTestIdOverrideFixture.class);

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getName(), is("annotatedTest() (TestId: METHOD-TC)"));
    assertThat(results.get(0).getTestCaseId(), is("METHOD-TC"));
  }
}
