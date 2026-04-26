package com.teststeps.thekla4j.allure.cucumber.plugins;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teststeps.thekla4j.cucumber.Thekla4jWorld;
import io.cucumber.core.options.CommandlineOptionsParser;
import io.cucumber.core.options.RuntimeOptions;
import io.cucumber.core.runtime.Runtime;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.Plugin;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseFinished;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.FileSystemResultsWriter;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.test.AllureResultsWriterStub;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Integration tests for {@link Thekla4jAllureCucumberPlugin}. Runs actual Cucumber feature files
 * via the Cucumber Runtime API and verifies the Allure model output.
 */
class Thekla4jAllureCucumberPluginIntegrationTest {

  private static final String STEP_DEFS_PACKAGE =
      "com.teststeps.thekla4j.allure.cucumber.test.stepdefs";
  private static final String ACTIVITY_LOG_STEP_DEFS_PACKAGE =
      "com.teststeps.thekla4j.allure.cucumber.test.activitylog";
  private static final String THEKLA_REQUIREMENT_LINK_PATTERN_PROPERTY = "thekla4j.req.link.issue.pattern";

  // ===== Helpers =====

  private List<TestResult> runFeature(final String featurePath) {
    return runFeature(featurePath, List.of(STEP_DEFS_PACKAGE));
  }

  private List<TestResult> runFeature(
                                      final String featurePath, final List<String> gluePackages, final Plugin... additionalPlugins
  ) {
    final AllureResultsWriterStub stub = new AllureResultsWriterStub();
    final AllureLifecycle lifecycle = new AllureLifecycle(stub);
    final Thekla4jAllureCucumberPlugin plugin = new Thekla4jAllureCucumberPlugin(lifecycle);

    runCucumber(featurePath, plugin, gluePackages, additionalPlugins);
    return stub.getTestResults();
  }

  private Path runFeatureToDir(final String featurePath, final Path resultsDir) {
    return runFeatureToDir(featurePath, resultsDir, List.of(STEP_DEFS_PACKAGE));
  }

  private Path runFeatureToDir(
                               final String featurePath, final Path resultsDir, final List<String> gluePackages, final Plugin... additionalPlugins
  ) {
    final FileSystemResultsWriter writer = new FileSystemResultsWriter(resultsDir);
    final AllureLifecycle lifecycle = new AllureLifecycle(writer);
    final Thekla4jAllureCucumberPlugin plugin = new Thekla4jAllureCucumberPlugin(lifecycle);

    runCucumber(featurePath, plugin, gluePackages, additionalPlugins);
    return resultsDir;
  }

  private void runCucumber(
                           final String featurePath, final Thekla4jAllureCucumberPlugin plugin, final List<String> gluePackages, final Plugin... additionalPlugins
  ) {
    final List<String> commandLineArgs = new ArrayList<>();
    commandLineArgs.add(featurePath);
    for (final String gluePackage : gluePackages) {
      commandLineArgs.add("--glue");
      commandLineArgs.add(gluePackage);
    }
    commandLineArgs.add("--no-publish");

    final RuntimeOptions options =
        new CommandlineOptionsParser(OutputStream.nullOutputStream())
            .parse(commandLineArgs.toArray(new String[0]))
            .build();

    final Plugin[] plugins = new Plugin[additionalPlugins.length + 1];
    plugins[0] = plugin;
    if (additionalPlugins.length > 0) {
      System.arraycopy(additionalPlugins, 0, plugins, 1, additionalPlugins.length);
    }

    Runtime.builder()
        .withRuntimeOptions(options)
        .withClassLoader(getClass()::getClassLoader)
        .withAdditionalPlugins(plugins)
        .build()
        .run();
  }

  private String labelValue(final TestResult result, final String labelName) {
    return result.getLabels()
        .stream()
        .filter(l -> labelName.equals(l.getName()))
        .map(Label::getValue)
        .findFirst()
        .orElse(null);
  }

  private List<String> linkNamesByType(final TestResult result, final String linkType) {
    return result.getLinks()
        .stream()
        .filter(link -> linkType.equalsIgnoreCase(link.getType()))
        .map(link -> link.getName())
        .collect(Collectors.toList());
  }

  private JsonNode readResultJson(final Path resultsDir) throws IOException {
    final List<Path> resultFiles =
        Files.list(resultsDir)
            .filter(p -> p.getFileName().toString().endsWith("-result.json"))
            .collect(Collectors.toList());

    assertThat("expected at least one result JSON file", resultFiles.size(), greaterThanOrEqualTo(1));
    return new ObjectMapper().readTree(resultFiles.get(0).toFile());
  }

  // ===== Passing scenario =====

  @Test
  void passingScenario_shouldHavePassedStatus() {
    final List<TestResult> results = runFeature("classpath:features/passing_scenario.feature");

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getStatus(), is(Status.PASSED));
  }

  @Test
  void passingScenario_nameIsScenarioName() {
    final List<TestResult> results = runFeature("classpath:features/passing_scenario.feature");

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getName(), is("A simple passing scenario"));
  }

  // ===== Assertion error =====

  @Test
  void assertionError_shouldResultInFailedStatus() {
    final List<TestResult> results =
        runFeature("classpath:features/failing_assertion_scenario.feature");

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getStatus(), is(Status.FAILED));
  }

  @Test
  void assertionError_statusDetails_shouldContainMessage() {
    final List<TestResult> results =
        runFeature("classpath:features/failing_assertion_scenario.feature");

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getStatusDetails(), notNullValue());
    assertThat(
      results.get(0).getStatusDetails().getMessage(),
      containsString("Expected condition not met"));
  }

  // ===== Activity error =====

  @Test
  void activityError_shouldResultInFailedStatus() {
    final List<TestResult> results =
        runFeature("classpath:features/activity_error_scenario.feature");

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getStatus(), is(Status.FAILED));
  }

  @Test
  void activityError_statusDetails_shouldContainMessage() {
    final List<TestResult> results =
        runFeature("classpath:features/activity_error_scenario.feature");

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getStatusDetails(), notNullValue());
    assertThat(
      results.get(0).getStatusDetails().getMessage(),
      containsString("Activity failed during test step"));
  }

  @Test
  void activityError_statusDetails_shouldContainStackTrace() {
    final List<TestResult> results =
        runFeature("classpath:features/activity_error_scenario.feature");

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getStatusDetails().getTrace(), notNullValue());
    assertThat(
      results.get(0).getStatusDetails().getTrace(),
      containsString("ActivityError"));
  }

  // ===== Tagged scenario – labels =====

  @Test
  void taggedScenario_shouldHaveEpicLabel() {
    final List<TestResult> results = runFeature("classpath:features/tagged_scenario.feature");

    final TestResult tagged = results.stream()
        .filter(r -> r.getName() != null && r.getName().contains("TC-001"))
        .findFirst()
        .orElseThrow(() -> new AssertionError("tagged scenario not found"));

    assertThat(labelValue(tagged, "epic"), is("TestEpic"));
  }

  @Test
  void taggedScenario_shouldHaveStoryLabel() {
    final List<TestResult> results = runFeature("classpath:features/tagged_scenario.feature");

    final TestResult tagged = results.stream()
        .filter(r -> r.getName() != null && r.getName().contains("TC-001"))
        .findFirst()
        .orElseThrow(() -> new AssertionError("tagged scenario not found"));

    assertThat(labelValue(tagged, "story"), is("TestStory"));
  }

  @Test
  void taggedScenario_shouldHaveSuiteLabel() {
    final List<TestResult> results = runFeature("classpath:features/tagged_scenario.feature");

    final TestResult tagged = results.stream()
        .filter(r -> r.getName() != null && r.getName().contains("TC-001"))
        .findFirst()
        .orElseThrow(() -> new AssertionError("tagged scenario not found"));

    assertThat(labelValue(tagged, "suite"), is("TestSuite"));
  }

  @Test
  void taggedScenario_shouldHaveSeverityLabel() {
    final List<TestResult> results = runFeature("classpath:features/tagged_scenario.feature");

    final TestResult tagged = results.stream()
        .filter(r -> r.getName() != null && r.getName().contains("TC-001"))
        .findFirst()
        .orElseThrow(() -> new AssertionError("tagged scenario not found"));

    assertThat(labelValue(tagged, "severity"), is("critical"));
  }

  @Test
  void taggedScenario_shouldHaveRequirementLinks() {
    final List<TestResult> results = runFeature("classpath:features/tagged_scenario.feature");

    final TestResult tagged = results.stream()
        .filter(r -> r.getName() != null && r.getName().contains("TC-001"))
        .findFirst()
        .orElseThrow(() -> new AssertionError("tagged scenario not found"));

    assertThat(linkNamesByType(tagged, "requirement"), is(List.of("REQ-001", "REQ-002")));
  }

  @Test
  void scenarioWithBlankSingularRequirementTag_shouldNotHaveRequirementLinks() {
    final List<TestResult> results = runFeature("classpath:features/blank_singular_req_tag.feature");

    assertThat(results, hasSize(1));
    assertThat(linkNamesByType(results.get(0), "requirement"), is(List.of()));
  }

  // ===== TEST_ID in name =====

  @Test
  void taggedScenario_withTestId_shouldIncludeTestIdInName() {
    final List<TestResult> results = runFeature("classpath:features/tagged_scenario.feature");

    final TestResult tagged = results.stream()
        .filter(r -> r.getName() != null && r.getName().contains("TC-001"))
        .findFirst()
        .orElseThrow(() -> new AssertionError("scenario with testId not found"));

    assertThat(tagged.getName(), containsString("(testId: TC-001)"));
    assertThat(tagged.getTestCaseId(), is("TC-001"));
  }

  @Test
  void scenarioWithoutTestId_shouldNotHaveTestIdSuffix() {
    final List<TestResult> results = runFeature("classpath:features/tagged_scenario.feature");

    final TestResult noTag = results.stream()
        .filter(r -> r.getName() != null && !r.getName().contains("TC-001"))
        .findFirst()
        .orElseThrow(() -> new AssertionError("scenario without testId not found"));

    assertThat(noTag.getName(), not(containsString("(testId:")));
    assertThat(noTag.getTestCaseId(), nullValue());
  }

  // ===== Scenario outline parameters =====

  @Test
  void scenarioOutline_shouldHaveTwoResults() {
    final List<TestResult> results =
        runFeature("classpath:features/scenario_outline.feature");

    assertThat(results, hasSize(2));
  }

  @Test
  void scenarioOutline_shouldHaveParametersSet() {
    final List<TestResult> results =
        runFeature("classpath:features/scenario_outline.feature");

    final List<String> paramValues =
        results.stream()
            .flatMap(r -> r.getParameters().stream())
            .filter(p -> "value".equals(p.getName()))
            .map(Parameter::getValue)
            .collect(Collectors.toList());

    assertThat(paramValues, hasSize(2));
    assertThat(paramValues.contains("foo"), is(true));
    assertThat(paramValues.contains("bar"), is(true));
  }

  @Test
  void activityLogScenario_shouldBeReportedWhenThreadLocalWorldIsClearedBeforeCaseFinished() {
    final List<TestResult> results =
        runFeature(
          "classpath:features/activity_log_world_scenario.feature",
          List.of(ACTIVITY_LOG_STEP_DEFS_PACKAGE),
          new ClearWorldBeforeCaseFinishedPlugin());

    assertThat(results, hasSize(1));
    assertThat(results.get(0).getSteps(), notNullValue());

    final boolean hasActivityLogSection = results.get(0)
        .getSteps()
        .stream()
        .map(step -> step.getName() == null ? "" : step.getName())
        .anyMatch(name -> name.contains("Activity Log"));
    assertThat("Expected an Activity Log section in the Allure test steps", hasActivityLogSection, is(true));
  }

  // ===== JSON file output =====

  @Test
  void fileOutput_passing_isWrittenToResultJson(@TempDir final Path tempDir) throws IOException {
    runFeatureToDir("classpath:features/passing_scenario.feature", tempDir);

    final JsonNode root = readResultJson(tempDir);

    assertThat(root.path("status").asText(), is("passed"));
    assertThat(root.path("name").asText(), is("A simple passing scenario"));
  }

  @Test
  void fileOutput_epicLabel_isWrittenToResultJson(@TempDir final Path tempDir) throws IOException {
    runFeatureToDir("classpath:features/tagged_scenario.feature", tempDir);

    final List<Path> resultFiles =
        Files.list(tempDir)
            .filter(p -> p.getFileName().toString().endsWith("-result.json"))
            .collect(Collectors.toList());

    assertThat(
      "expected two result JSON files (one per scenario)",
      resultFiles.size(),
      greaterThanOrEqualTo(1));

    final ObjectMapper mapper = new ObjectMapper();
    final boolean anyHasEpic =
        resultFiles.stream()
            .map(
              path -> {
                try {
                  return mapper.readTree(path.toFile());
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              })
            .anyMatch(
              json -> {
                final JsonNode labels = json.path("labels");
                for (final JsonNode label : labels) {
                  if ("epic".equals(label.path("name").asText()) && "TestEpic".equals(label.path("value").asText())) {
                    return true;
                  }
                }
                return false;
              });

    assertThat("epic label with value 'TestEpic' should appear in a result JSON", anyHasEpic, is(true));
  }

  @Test
  void fileOutput_requirementLinks_areWrittenToResultJson(@TempDir final Path tempDir) throws IOException {
    runFeatureToDir("classpath:features/tagged_scenario.feature", tempDir);

    final List<Path> resultFiles =
        Files.list(tempDir)
            .filter(p -> p.getFileName().toString().endsWith("-result.json"))
            .collect(Collectors.toList());

    assertThat(
      "expected two result JSON files (one per scenario)",
      resultFiles.size(),
      greaterThanOrEqualTo(1));

    final ObjectMapper mapper = new ObjectMapper();
    final boolean anyHasRequirementLinks =
        resultFiles.stream()
            .map(
              path -> {
                try {
                  return mapper.readTree(path.toFile());
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              })
            .anyMatch(
              json -> {
                final JsonNode links = json.path("links");
                boolean hasReq001 = false;
                boolean hasReq002 = false;
                for (final JsonNode link : links) {
                  if ("requirement".equals(link.path("type").asText())) {
                    if ("REQ-001".equals(link.path("name").asText())) {
                      hasReq001 = true;
                    }
                    if ("REQ-002".equals(link.path("name").asText())) {
                      hasReq002 = true;
                    }
                  }
                }
                return hasReq001 && hasReq002;
              });

    assertThat("requirement links should appear in a result JSON", anyHasRequirementLinks, is(true));
  }

  @Test
  void fileOutput_requirementLinks_useTheklaRequirementPattern(@TempDir final Path tempDir) throws IOException {
    final String previousPattern = System.getProperty(THEKLA_REQUIREMENT_LINK_PATTERN_PROPERTY);
    System.setProperty(THEKLA_REQUIREMENT_LINK_PATTERN_PROPERTY, "https://req.local/{}");
    try {
      runFeatureToDir("classpath:features/tagged_scenario.feature", tempDir);

      final List<Path> resultFiles =
          Files.list(tempDir)
              .filter(p -> p.getFileName().toString().endsWith("-result.json"))
              .collect(Collectors.toList());

      assertThat(
        "expected two result JSON files (one per scenario)",
        resultFiles.size(),
        greaterThanOrEqualTo(1));

      final ObjectMapper mapper = new ObjectMapper();
      final List<String> requirementUrls =
          resultFiles.stream()
              .map(
                path -> {
                  try {
                    return mapper.readTree(path.toFile());
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                })
              .flatMap(
                json -> {
                  final List<String> urls = new java.util.ArrayList<>();
                  for (final JsonNode link : json.path("links")) {
                    if ("requirement".equals(link.path("type").asText())) {
                      urls.add(link.path("url").asText());
                    }
                  }
                  return urls.stream();
                })
              .collect(Collectors.toList());

      assertThat(
        "requirement links should include resolved URL for REQ-001 in result JSON",
        requirementUrls.contains("https://req.local/REQ-001"),
        is(true));
      assertThat(
        "requirement links should include resolved URL for REQ-002 in result JSON",
        requirementUrls.contains("https://req.local/REQ-002"),
        is(true));
    } finally {
      if (previousPattern == null) {
        System.clearProperty(THEKLA_REQUIREMENT_LINK_PATTERN_PROPERTY);
      } else {
        System.setProperty(THEKLA_REQUIREMENT_LINK_PATTERN_PROPERTY, previousPattern);
      }
    }
  }

  @Test
  void fileOutput_activityError_isWrittenAsFailedInResultJson(@TempDir final Path tempDir) throws IOException {
    runFeatureToDir("classpath:features/activity_error_scenario.feature", tempDir);

    final JsonNode root = readResultJson(tempDir);

    assertThat(root.path("status").asText(), is("failed"));
    assertThat(
      root.path("statusDetails").path("message").asText(),
      containsString("Activity failed during test step"));
    assertThat(
      root.path("statusDetails").path("trace").asText(),
      containsString("ActivityError"));
  }

  private static final class ClearWorldBeforeCaseFinishedPlugin implements ConcurrentEventListener {

    private final EventHandler<TestCaseFinished> caseFinishedHandler =
        event -> Thekla4jWorld.clearCurrentWorld();

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
      publisher.registerHandlerFor(TestCaseFinished.class, caseFinishedHandler);
    }
  }
}
